package com.slideink.protocol

/**
 * Protocolo de Comunicação SlideInk
 * 
 * Define todos os comandos e estruturas de dados trocados entre
 * Android (controle) e Windows (apresentação).
 * 
 * Este protocolo é agnóstico ao transporte (USB, Wi-Fi, Bluetooth).
 */

// Tipos de mensagem do protocolo
enum class MessageType(val code: String) {
    // Controle de apresentação
    NEXT_SLIDE("next_slide"),
    PREVIOUS_SLIDE("previous_slide"),
    GOTO_SLIDE("goto_slide"),
    START_PRESENTATION("start_presentation"),
    END_PRESENTATION("end_presentation"),
    
    // Ferramentas
    LASER_ON("laser_on"),
    LASER_OFF("laser_off"),
    LASER_MOVE("laser_move"),
    
    // Anotação - Caneta
    TOOL_PEN("tool_pen"),
    TOOL_ERASER("tool_eraser"),
    TOOL_HIGHLIGHTER("tool_highlighter"),
    
    // Configurações da caneta
    PEN_COLOR("pen_color"),
    PEN_SIZE("pen_size"),
    ERASER_SIZE("eraser_size"),
    
    // Strokes (traços)
    STROKE_START("stroke_start"),
    STROKE_POINT("stroke_point"),
    STROKE_END("stroke_end"),
    
    // Limpeza
    CLEAR_ANNOTATIONS("clear_annotations"),
    UNDO_STROKE("undo_stroke"),
    
    // Estado e conexão
    CONNECTION_STATUS("connection_status"),
    HEARTBEAT("heartbeat"),
    PAGE_CHANGED("page_changed"),
    
    // Erros
    ERROR("error");
    
    companion object {
        fun fromCode(code: String): MessageType? = values().find { it.code == code }
    }
}

// Estrutura base de mensagem
data class SlideInkMessage(
    val type: MessageType,
    val timestamp: Long = System.currentTimeMillis(),
    val payload: Map<String, Any> = emptyMap()
) {
    fun toJson(): String {
        // Implementação simplificada - na prática usar Gson/Moshi
        return buildString {
            append("{")
            append("\"type\":\"${type.code}\"")
            append(",\"timestamp\":$timestamp")
            if (payload.isNotEmpty()) {
                append(",\"payload\":{")
                payload.entries.joinToString(",") { (key, value) ->
                    "\"$key\":\"$value\""
                }.also { append(it) }
                append("}")
            }
            append("}")
        }
    }
    
    companion object {
        fun fromJson(json: String): SlideInkMessage? {
            // Implementação simplificada - na prática usar Gson/Moshi
            // Parser básico para demonstração
            return try {
                val typeRegex = "\"type\":\"([^\"]+)\"".toRegex()
                val timestampRegex = "\"timestamp\":(\\d+)".toRegex()
                
                val typeMatch = typeRegex.find(json)
                val timestampMatch = timestampRegex.find(json)
                
                val type = typeMatch?.groupValues?.get(1)?.let { MessageType.fromCode(it) } ?: return null
                val timestamp = timestampMatch?.groupValues?.get(1)?.toLongOrNull() ?: System.currentTimeMillis()
                
                SlideInkMessage(type, timestamp)
            } catch (e: Exception) {
                null
            }
        }
    }
}

// Mensagens específicas para controle de slides
data class GoToSlideMessage(val pageNumber: Int, val totalPages: Int = -1) {
    fun toSlideInkMessage(): SlideInkMessage {
        return SlideInkMessage(
            MessageType.GOTO_SLIDE,
            payload = mapOf(
                "page" to pageNumber,
                "total" to totalPages
            )
        )
    }
}

// Mensagens para laser
data class LaserPositionMessage(val x: Float, val y: Float) {
    fun toSlideInkMessage(): SlideInkMessage {
        return SlideInkMessage(
            MessageType.LASER_MOVE,
            payload = mapOf(
                "x" to x.toString(),
                "y" to y.toString()
            )
        )
    }
}

// Mensagens para strokes (anotações)
data class StrokePoint(
    val x: Float,
    val y: Float,
    val pressure: Float = 1.0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class StrokeStartMessage(
    val strokeId: String,
    val x: Float,
    val y: Float,
    val color: Int, // ARGB
    val size: Float,
    val tool: String // "pen", "highlighter"
) {
    fun toSlideInkMessage(): SlideInkMessage {
        return SlideInkMessage(
            MessageType.STROKE_START,
            payload = mapOf(
                "strokeId" to strokeId,
                "x" to x.toString(),
                "y" to y.toString(),
                "color" to color.toString(),
                "size" to size.toString(),
                "tool" to tool
            )
        )
    }
}

data class StrokePointMessage(
    val strokeId: String,
    val points: List<StrokePoint>
) {
    fun toSlideInkMessage(): SlideInkMessage {
        val pointsJson = points.joinToString(";") { "${it.x},${it.y},${it.pressure}" }
        return SlideInkMessage(
            MessageType.STROKE_POINT,
            payload = mapOf(
                "strokeId" to strokeId,
                "points" to pointsJson
            )
        )
    }
}

data class StrokeEndMessage(val strokeId: String) {
    fun toSlideInkMessage(): SlideInkMessage {
        return SlideInkMessage(
            MessageType.STROKE_END,
            payload = mapOf("strokeId" to strokeId)
        )
    }
}

// Mensagens de configuração
data class PenConfigMessage(
    val color: Int, // ARGB
    val size: Float,
    val tool: String // "pen", "eraser", "highlighter"
) {
    fun toSlideInkMessage(): SlideInkMessage {
        val type = when (tool) {
            "pen" -> MessageType.TOOL_PEN
            "eraser" -> MessageType.TOOL_ERASER
            "highlighter" -> MessageType.TOOL_HIGHLIGHTER
            else -> MessageType.TOOL_PEN
        }
        
        return SlideInkMessage(
            type,
            payload = mapOf(
                "color" to color.toString(),
                "size" to size.toString()
            )
        )
    }
}

// Mensagens de estado
data class ConnectionStatusMessage(
    val status: String, // "connected", "disconnected", "connecting"
    val transport: String, // "usb", "wifi", "bluetooth"
    val latency: Int? = null // em ms
) {
    fun toSlideInkMessage(): SlideInkMessage {
        val payload = mutableMapOf(
            "status" to status,
            "transport" to transport
        )
        latency?.let { payload["latency"] = it.toString() }
        
        return SlideInkMessage(
            MessageType.CONNECTION_STATUS,
            payload = payload
        )
    }
}

data class PageChangedMessage(
    val currentPage: Int,
    val totalPages: Int
) {
    fun toSlideInkMessage(): SlideInkMessage {
        return SlideInkMessage(
            MessageType.PAGE_CHANGED,
            payload = mapOf(
                "current" to currentPage.toString(),
                "total" to totalPages.toString()
            )
        )
    }
}

// Heartbeat para manter conexão ativa
data class HeartbeatMessage(val counter: Long = System.currentTimeMillis()) {
    fun toSlideInkMessage(): SlideInkMessage {
        return SlideInkMessage(
            MessageType.HEARTBEAT,
            payload = mapOf("counter" to counter.toString())
        )
    }
}

// Mensagem de erro
data class ErrorMessage(
    val code: String,
    val message: String,
    val details: String? = null
) {
    fun toSlideInkMessage(): SlideInkMessage {
        val payload = mutableMapOf(
            "code" to code,
            "message" to message
        )
        details?.let { payload["details"] = it }
        
        return SlideInkMessage(
            MessageType.ERROR,
            payload = payload
        )
    }
}

/**
 * Utilitários para serialização/desserialização
 */
object ProtocolSerializer {
    
    fun serialize(message: SlideInkMessage): String {
        return message.toJson()
    }
    
    fun deserialize(json: String): SlideInkMessage? {
        return SlideInkMessage.fromJson(json)
    }
    
    /**
     * Valida se uma mensagem está bem formada
     */
    fun validate(message: SlideInkMessage): Boolean {
        return when (message.type) {
            MessageType.GOTO_SLIDE -> message.payload.containsKey("page")
            MessageType.STROKE_START -> message.payload.containsKey("strokeId") && 
                                       message.payload.containsKey("x") &&
                                       message.payload.containsKey("y")
            MessageType.STROKE_POINT -> message.payload.containsKey("strokeId") &&
                                        message.payload.containsKey("points")
            MessageType.LASER_MOVE -> message.payload.containsKey("x") &&
                                      message.payload.containsKey("y")
            else -> true
        }
    }
}
