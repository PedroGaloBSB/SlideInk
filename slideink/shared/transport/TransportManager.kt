package com.slideink.transport

import com.slideink.protocol.SlideInkMessage
import com.slideink.protocol.ProtocolSerializer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

/**
 * TransportManager - Gerenciador de Transportes
 * 
 * Implementa o conceito do Documento Consolidado:
 * - Descobre conexões disponíveis
 * - Estabelece conexão
 * - Acompanha qualidade
 * - Seleciona transporte automaticamente ou manualmente
 * - Informa estado para a interface
 * - Gerencia failover (objetivo arquitetural)
 */
class TransportManager : CoroutineScope {
    
    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job
    
    // Lista de transportes registrados
    private val transports = mutableMapOf<TransportType, Transport>()
    
    // Transporte atualmente ativo
    private var activeTransport: Transport? = null
    
    // Estado atual do gerenciamento
    private val _managerState = MutableStateFlow(ManagerState.Disconnected)
    val managerState: StateFlow<ManagerState> = _managerState.asStateFlow()
    
    // Métricas atuais
    private val _currentMetrics = MutableStateFlow<TransportMetrics?>(null)
    val currentMetrics: StateFlow<TransportMetrics?> = _currentMetrics.asStateFlow()
    
    // Configurações
    private var autoSelectEnabled = true
    private var preferredTransport: TransportType? = null
    
    // Listener para eventos
    private var listener: TransportListener? = null
    
    // Heartbeat
    private var heartbeatJob: Job? = null
    private val heartbeatIntervalMs = 5000L
    
    init {
        launch {
            monitorConnectionQuality()
        }
    }
    
    /**
     * Registra um transporte no gerenciador
     */
    fun registerTransport(transport: Transport) {
        transports[transport.type] = transport
        launch {
            monitorTransportState(transport)
        }
    }
    
    /**
     * Remove um transporte do gerenciador
     */
    fun unregisterTransport(type: TransportType) {
        transports.remove(type)?.let { transport ->
            runBlocking { transport.disconnect() }
        }
    }
    
    /**
     * Configura modo de seleção automática
     */
    fun setAutoSelect(enabled: Boolean) {
        autoSelectEnabled = enabled
        if (!enabled) {
            _managerState.value = ManagerState.ManualSelection
        }
    }
    
    /**
     * Define transporte preferencial (para modo manual)
     */
    fun setPreferredTransport(type: TransportType?) {
        preferredTransport = type
    }
    
    /**
     * Inicia conexão usando o melhor transporte disponível
     */
    suspend fun connect(): Result<Unit> {
        return if (autoSelectEnabled) {
            connectBestAvailable()
        } else {
            preferredTransport?.let { connectTo(it) }
                ?: Result.failure(TransportException.InvalidConfig("No preferred transport set"))
        }
    }
    
    /**
     * Conecta a um transporte específico
     */
    suspend fun connectTo(type: TransportType): Result<Unit> {
        val transport = transports[type]
            ?: return Result.failure(TransportException.NotAvailable(type))
        
        if (!transport.isAvailable()) {
            return Result.failure(TransportException.NotAvailable(type))
        }
        
        _managerState.value = ManagerState.Connecting(type)
        
        return transport.connect()
            .onSuccess {
                activeTransport = transport
                _managerState.value = ManagerState.Connected(type)
                startHeartbeat()
            }
            .onFailure { error ->
                _managerState.value = ManagerState.Error(
                    "Failed to connect to ${type.name}: ${error.message}",
                    type
                )
            }
    }
    
    /**
     * Seleciona e conecta ao melhor transporte disponível
     */
    private suspend fun connectBestAvailable(): Result<Unit> {
        // Ordena transportes por prioridade e disponibilidade
        val availableTransports = transports.values
            .filter { it.isAvailable() }
            .sortedBy { getTransportPriority(it.type) }
        
        if (availableTransports.isEmpty()) {
            return Result.failure(TransportException.NotAvailable(TransportType.USB))
        }
        
        _managerState.value = ManagerState.Selecting
        
        // Tenta conectar em ordem de prioridade
        for (transport in availableTransports) {
            _managerState.value = ManagerState.Testing(transport.type)
            
            val result = transport.connect()
            if (result.isSuccess) {
                activeTransport = transport
                _managerState.value = ManagerState.Connected(transport.type)
                startHeartbeat()
                return Result.success(Unit)
            }
        }
        
        return Result.failure(TransportException.ConnectionFailed(
            "No available transport could establish connection"
        ))
    }
    
    /**
     * Prioridade dos transportes (menor = mais prioritário)
     */
    private fun getTransportPriority(type: TransportType): Int {
        return when (type) {
            TransportType.USB -> 1           // Mais estável
            TransportType.WIFI -> 2          // MVP sem fio
            TransportType.BLUETOOTH_CLASSIC -> 3
            TransportType.BLUETOOTH_LE -> 4
            TransportType.HOTSPOT -> 5
        }
    }
    
    /**
     * Desconecta do transporte atual
     */
    suspend fun disconnect() {
        stopHeartbeat()
        
        activeTransport?.let { transport ->
            _managerState.value = ManagerState.Disconnecting
            transport.disconnect()
            activeTransport = null
            _managerState.value = ManagerState.Disconnected
        }
    }
    
    /**
     * Envia mensagem através do transporte ativo
     */
    suspend fun send(message: SlideInkMessage): Result<Unit> {
        val transport = activeTransport
            ?: return Result.failure(TransportException.ConnectionFailed("No active transport"))
        
        // Valida mensagem antes de enviar
        if (!ProtocolSerializer.validate(message)) {
            return Result.failure(TransportException.SendFailed("Invalid message format"))
        }
        
        return transport.send(message)
            .onFailure { error ->
                _managerState.value = ManagerState.Error(
                    "Send failed: ${error.message}",
                    transport.type
                )
            }
    }
    
    /**
     * Envia comando simplificado
     */
    suspend fun sendCommand(command: String, payload: Map<String, Any> = emptyMap()): Result<Unit> {
        val message = SlideInkMessage(
            type = com.slideink.protocol.MessageType.fromCode(command) 
                ?: return Result.failure(TransportException.SendFailed("Unknown command: $command")),
            payload = payload
        )
        return send(message)
    }
    
    /**
     * Inicia heartbeat para manter conexão ativa e medir latência
     */
    private fun startHeartbeat() {
        stopHeartbeat()
        heartbeatJob = launch {
            var counter = 0L
            while (isActive && activeTransport != null) {
                val heartbeatMsg = com.slideink.protocol.HeartbeatMessage(counter).toSlideInkMessage()
                send(heartbeatMsg)
                counter++
                delay(heartbeatIntervalMs)
            }
        }
    }
    
    /**
     * Para heartbeat
     */
    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }
    
    /**
     * Monitora estado de um transporte específico
     */
    private suspend fun monitorTransportState(transport: Transport) {
        transport.state.collect { state ->
            when (state) {
                is TransportState.Error -> {
                    if (transport == activeTransport) {
                        _managerState.value = ManagerState.Error(state.message, transport.type)
                        handleConnectionLoss(transport)
                    }
                }
                TransportState.Disconnected -> {
                    if (transport == activeTransport) {
                        _managerState.value = ManagerState.Disconnected
                        activeTransport = null
                    }
                }
                else -> {}
            }
        }
    }
    
    /**
     * Monitora qualidade da conexão ativa
     */
    private suspend fun monitorConnectionQuality() {
        while (isActive) {
            activeTransport?.let { transport ->
                transport.currentLatencyMs?.let { latency ->
                    val metrics = TransportMetrics(
                        latencyMs = latency,
                        jitterMs = estimateJitter(transport),
                        packetLossPercent = estimatePacketLoss(transport),
                        bandwidthKbps = estimateBandwidth(transport)
                    )
                    _currentMetrics.value = metrics
                    
                    // Verifica se precisa de failover
                    if (!metrics.isAcceptableForRealTime() && autoSelectEnabled) {
                        // TODO: Implementar failover inteligente
                        // Por enquanto apenas notifica
                        _managerState.value = ManagerState.QualityDegraded(metrics, transport.type)
                    }
                }
            }
            delay(2000)
        }
    }
    
    /**
     * Lida com perda de conexão - tenta failover
     */
    private suspend fun handleConnectionLoss(failedTransport: Transport) {
        if (!autoSelectEnabled) {
            return
        }
        
        _managerState.value = ManagerState.FailoverAttempting
        
        // Tenta encontrar transporte alternativo
        val alternative = transports.values
            .firstOrNull { it != failedTransport && it.isAvailable() }
        
        if (alternative != null) {
            val result = alternative.connect()
            if (result.isSuccess) {
                activeTransport = alternative
                _managerState.value = ManagerState.FailoverSuccess(alternative.type)
                startHeartbeat()
                return
            }
        }
        
        _managerState.value = ManagerState.FailoverFailed
    }
    
    /**
     * Define listener para eventos
     */
    fun setListener(listener: TransportListener) {
        this.listener = listener
    }
    
    /**
     * Retorna lista de transportes disponíveis
     */
    fun getAvailableTransports(): List<TransportInfo> {
        return transports.values
            .filter { it.isAvailable() }
            .map { transport ->
                TransportInfo(
                    type = transport.type,
                    name = transport.name,
                    isAvailable = true,
                    isActive = transport == activeTransport,
                    quality = transport.quality
                )
            }
    }
    
    /**
     * Estimativas de métricas (implementação real deve medir)
     */
    private suspend fun estimateJitter(transport: Transport): Int {
        // TODO: Implementar medição real de jitter
        return transport.currentLatencyMs?.let { (it * 0.2).toInt() } ?: 20
    }
    
    private suspend fun estimatePacketLoss(transport: Transport): Double {
        // TODO: Implementar medição real de packet loss
        return 0.5
    }
    
    private suspend fun estimateBandwidth(transport: Transport): Long {
        // TODO: Implementar medição real de bandwidth
        return when (transport.type) {
            TransportType.USB -> 10_000 // 10 Mbps
            TransportType.WIFI -> 5_000 // 5 Mbps
            TransportType.BLUETOOTH_CLASSIC -> 2_000 // 2 Mbps
            TransportType.BLUETOOTH_LE -> 100 // 100 Kbps
            TransportType.HOTSPOT -> 3_000 // 3 Mbps
        }
    }
    
    /**
     * Limpa recursos
     */
    fun cleanup() {
        stopHeartbeat()
        runBlocking {
            transports.values.forEach { it.disconnect() }
        }
        job.cancel()
    }
}

/**
 * Estados do TransportManager
 */
sealed class ManagerState {
    object Disconnected : ManagerState()
    object Selecting : ManagerState()
    data class Connecting(val type: TransportType) : ManagerState()
    data class Testing(val type: TransportType) : ManagerState()
    data class Connected(val type: TransportType) : ManagerState()
    object Disconnecting : ManagerState()
    object ManualSelection : ManagerState()
    data class QualityDegraded(val metrics: TransportMetrics, val type: TransportType) : ManagerState()
    object FailoverAttempting : ManagerState()
    data class FailoverSuccess(val type: TransportType) : ManagerState()
    object FailoverFailed : ManagerState()
    data class Error(val message: String, val type: TransportType) : ManagerState()
}

/**
 * Informações sobre um transporte para exibição na UI
 */
data class TransportInfo(
    val type: TransportType,
    val name: String,
    val isAvailable: Boolean,
    val isActive: Boolean,
    val quality: Int?
)
