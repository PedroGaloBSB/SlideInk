package com.slideink.transport.wifi

import com.slideink.protocol.SlideInkMessage
import com.slideink.protocol.ProtocolSerializer
import com.slideink.transport.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Implementação de transporte via Wi-Fi usando WebSocket
 * 
 * Prioridade: MVP (conforme Documento Consolidado)
 * - Principal caminho sem fio do MVP
 * - Não exige cabo
 * - Experiência mais natural
 */
class WifiTransport(
    private val config: TransportConfig.WifiConfig
) : Transport, CoroutineScope {
    
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job
    
    override val id: String = "wifi-${System.currentTimeMillis()}"
    override val name: String = "Wi-Fi"
    override val type: TransportType = TransportType.WIFI
    
    private val _state = MutableStateFlow<TransportState>(TransportState.Disconnected)
    override val state: StateFlow<TransportState> = _state.asStateFlow()
    
    override var currentLatencyMs: Int? = null
        private set
    
    override var quality: Int? = null
        private set
    
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .pingInterval(5, TimeUnit.SECONDS)
        .build()
    
    private var listener: TransportListener? = null
    private var lastLatencyCheck = 0L
    private val latencyHistory = mutableListOf<Long>()
    
    override fun isAvailable(): Boolean {
        // Verifica se há rede disponível
        // Implementação real deve verificar conectividade Android
        return true
    }
    
    override suspend fun connect(): Result<Unit> {
        if (_state.value == TransportState.Connected) {
            return Result.success(Unit)
        }
        
        _state.value = TransportState.Connecting
        
        return try {
            val url = buildUrl()
            val request = Request.Builder()
                .url(url)
                .build()
            
            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    _state.value = TransportState.Connected
                    startLatencyMonitoring()
                }
                
                override fun onMessage(webSocket: WebSocket, text: String) {
                    handleMessage(text)
                }
                
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    val error = TransportException.ConnectionFailed(t.message ?: "Unknown error")
                    _state.value = TransportState.Error(error.message)
                    listener?.onError(this@WifiTransport, error.message)
                }
                
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    _state.value = TransportState.Disconnected
                }
            })
            
            // Aguarda conexão ser estabelecida
            waitForConnection()
            
        } catch (e: Exception) {
            _state.value = TransportState.Error(e.message ?: "Connection failed")
            Result.failure(TransportException.ConnectionFailed(e.message ?: "Unknown error"))
        }
    }
    
    private fun buildUrl(): String {
        val scheme = if (config.useTLS) "wss" else "ws"
        return "$scheme://${config.host}:${config.port}/slideink"
    }
    
    private suspend fun waitForConnection(): Result<Unit> {
        val timeout = 10_000L
        val startTime = System.currentTimeMillis()
        
        while (System.currentTimeMillis() - startTime < timeout) {
            when (val currentState = _state.value) {
                is TransportState.Connected -> return Result.success(Unit)
                is TransportState.Error -> return Result.failure(
                    TransportException.ConnectionFailed(currentState.message)
                )
                else -> delay(100)
            }
        }
        
        return Result.failure(TransportException.Timeout(timeout))
    }
    
    override suspend fun disconnect() {
        _state.value = TransportState.Disconnecting
        
        stopLatencyMonitoring()
        webSocket?.close(1000, "Client disconnecting")
        webSocket = null
        
        _state.value = TransportState.Disconnected
    }
    
    override suspend fun send(message: SlideInkMessage): Result<Unit> {
        val ws = webSocket
            ?: return Result.failure(TransportException.SendFailed("Not connected"))
        
        return try {
            val json = ProtocolSerializer.serialize(message)
            ws.send(json)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(TransportException.SendFailed(e.message ?: "Send failed"))
        }
    }
    
    override suspend fun sendRaw(data: ByteArray): Result<Unit> {
        // WebSocket não suporta envio binário direto nesta implementação simples
        // Pode ser estendido no futuro
        return Result.failure(TransportException.SendFailed("Binary not supported"))
    }
    
    override suspend fun startListening() {
        // WebSocket já escuta automaticamente via onMessage
        // Este método é para compatibilidade com a interface
    }
    
    override suspend fun stopListening() {
        // WebSocket já gerencia internamente
    }
    
    private fun handleMessage(text: String) {
        val message = ProtocolSerializer.deserialize(text)
            ?: return
        
        // Atualiza latência se for heartbeat
        if (message.type == com.slideink.protocol.MessageType.HEARTBEAT) {
            updateLatency()
        }
        
        listener?.onMessageReceived(this, message)
    }
    
    private fun startLatencyMonitoring() {
        // Inicia monitoramento de latência via ping/pong
        launch {
            while (_state.value == TransportState.Connected) {
                updateLatency()
                delay(2000)
            }
        }
    }
    
    private fun stopLatencyMonitoring() {
        // Cancela monitoramento
    }
    
    private fun updateLatency() {
        val now = System.currentTimeMillis()
        if (lastLatencyCheck > 0) {
            val latency = now - lastLatencyCheck
            latencyHistory.add(latency)
            
            // Mantém apenas últimas 10 medidas
            if (latencyHistory.size > 10) {
                latencyHistory.removeAt(0)
            }
            
            // Calcula média
            currentLatencyMs = latencyHistory.average().toInt()
            quality = calculateQuality()
            
            listener?.onLatencyChanged(this, currentLatencyMs!!)
        }
        lastLatencyCheck = now
    }
    
    private fun calculateQuality(): Int {
        val latency = currentLatencyMs ?: return 50
        
        return when {
            latency < 30 -> 100
            latency < 60 -> 90
            latency < 100 -> 80
            latency < 150 -> 70
            latency < 200 -> 60
            latency < 300 -> 40
            else -> 20
        }
    }
    
    fun setListener(listener: TransportListener) {
        this.listener = listener
    }
    
    override fun equals(other?: Any?): Boolean {
        if (this === other) return true
        if (other !is WifiTransport) return false
        return id == other.id
    }
    
    override fun hashCode(): Int = id.hashCode()
}

/**
 * Factory para criar transportes Wi-Fi
 */
object WifiTransportFactory {
    
    /**
     * Cria um transporte Wi-Fi cliente (Android)
     */
    fun createClient(host: String, port: Int = 8765, useTLS: Boolean = false): WifiTransport {
        val config = TransportConfig.WifiConfig(
            host = host,
            port = port,
            useTLS = useTLS
        )
        return WifiTransport(config)
    }
    
    /**
     * Cria um transporte Wi-Fi servidor (Windows)
     * Nota: Implementação do servidor requer Ktor ou similar
     */
    fun createServer(port: Int = 8765): WifiTransport {
        // Para o servidor, precisamos de uma implementação diferente
        // Esta é uma stub para futura implementação
        val config = TransportConfig.WifiConfig(
            host = "0.0.0.0",
            port = port,
            useTLS = false
        )
        return WifiTransport(config)
    }
}
