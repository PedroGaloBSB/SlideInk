package com.slideink.transport

import com.slideink.protocol.SlideInkMessage
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface comum para todos os transportes (USB, Wi-Fi, Bluetooth)
 * 
 * Segue o princípio do Documento Consolidado:
 * "A aplicação não deve depender diretamente de uma tecnologia específica de conexão"
 */
interface Transport {
    
    /** Identificador único do transporte */
    val id: String
    
    /** Nome amigável para exibição */
    val name: String
    
    /** Tipo do transporte */
    val type: TransportType
    
    /** Estado atual da conexão */
    val state: StateFlow<TransportState>
    
    /** Latência atual em milissegundos (se disponível) */
    val currentLatencyMs: Int?
    
    /** Qualidade do sinal/conexão (0-100) */
    val quality: Int?
    
    /**
     * Estabelece conexão com o dispositivo remoto
     */
    suspend fun connect(): Result<Unit>
    
    /**
     * Fecha a conexão
     */
    suspend fun disconnect()
    
    /**
     * Envia uma mensagem
     * @return Result com sucesso ou erro
     */
    suspend fun send(message: SlideInkMessage): Result<Unit>
    
    /**
     * Envia dados brutos (para casos especiais)
     */
    suspend fun sendRaw(data: ByteArray): Result<Unit>
    
    /**
     * Verifica se o transporte está disponível neste dispositivo
     */
    fun isAvailable(): Boolean
    
    /**
     * Inicia escuta por mensagens recebidas
     * As mensagens recebidas são emitidas no flow onMessageReceived
     */
    suspend fun startListening()
    
    /**
     * Para de escutar mensagens
     */
    suspend fun stopListening()
}

/**
 * Tipos de transporte suportados
 */
enum class TransportType {
    USB,
    WIFI,
    BLUETOOTH_CLASSIC,
    BLUETOOTH_LE,
    HOTSPOT
}

/**
 * Estados possíveis de um transporte
 */
sealed class TransportState {
    object Disconnected : TransportState()
    object Connecting : TransportState()
    object Connected : TransportState()
    object Disconnecting : TransportState()
    data class Error(val message: String, val code: Int = -1) : TransportState()
}

/**
 * Callbacks para eventos do transporte
 */
interface TransportListener {
    fun onStateChanged(transport: Transport, state: TransportState)
    fun onMessageReceived(transport: Transport, message: SlideInkMessage)
    fun onLatencyChanged(transport: Transport, latencyMs: Int)
    fun onError(transport: Transport, error: String)
}

/**
 * Dados de configuração para cada tipo de transporte
 */
sealed class TransportConfig {
    data class UsbConfig(
        val vendorId: Int? = null,
        val productId: Int? = null
    ) : TransportConfig()
    
    data class WifiConfig(
        val host: String,
        val port: Int,
        val useTLS: Boolean = false
    ) : TransportConfig()
    
    data class BluetoothClassicConfig(
        val macAddress: String,
        val uuid: String
    ) : TransportConfig()
    
    data class BluetoothLEConfig(
        val deviceAddress: String,
        val serviceUuid: String,
        val characteristicUuid: String
    ) : TransportConfig()
    
    data class HotspotConfig(
        val ssid: String,
        val password: String,
        val port: Int
    ) : TransportConfig()
}

/**
 * Métricas de qualidade do transporte
 */
data class TransportMetrics(
    val latencyMs: Int,
    val jitterMs: Int,
    val packetLossPercent: Double,
    val bandwidthKbps: Long,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Calcula score de qualidade (0-100)
     */
    fun calculateQualityScore(): Int {
        // Fórmula simplificada - pode ser ajustada com dados reais
        var score = 100
        
        // Penaliza latência alta (>100ms começa a penalizar)
        if (latencyMs > 100) {
            score -= ((latencyMs - 100) / 10).coerceAtMost(30)
        }
        
        // Penaliza jitter alto (>50ms)
        if (jitterMs > 50) {
            score -= ((jitterMs - 50) / 10).coerceAtMost(25)
        }
        
        // Penaliza perda de pacote
        score -= (packetLossPercent * 2).toInt()
        
        return score.coerceIn(0, 100)
    }
    
    /**
     * Verifica se a qualidade é aceitável para anotação em tempo real
     */
    fun isAcceptableForRealTime(): Boolean {
        return latencyMs < 150 && 
               jitterMs < 80 && 
               packetLossPercent < 2.0
    }
    
    /**
     * Verifica se é adequado apenas para comandos (não para strokes)
     */
    fun isAcceptableForCommandsOnly(): Boolean {
        return latencyMs < 500 && 
               packetLossPercent < 5.0
    }
}

/**
 * Exceções específicas de transporte
 */
sealed class TransportException(message: String) : Exception(message) {
    class ConnectionFailed(reason: String) : TransportException("Connection failed: $reason")
    class SendFailed(reason: String) : TransportException("Send failed: $reason")
    class ReceiveFailed(reason: String) : TransportException("Receive failed: $reason")
    class Timeout(val timeoutMs: Long) : TransportException("Timeout after $timeoutMs ms")
    class NotAvailable(transportType: TransportType) : TransportException("$transportType not available")
    class InvalidConfig(reason: String) : TransportException("Invalid configuration: $reason")
}
