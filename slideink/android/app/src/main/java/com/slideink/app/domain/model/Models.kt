package com.slideink.android.domain.model

/**
 * Modelo de dados para um traço (stroke) de anotação
 */
data class Stroke(
    val id: String,
    val points: List<StrokePoint>,
    val color: Int, // ARGB
    val size: Float,
    val tool: ToolType,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Converte para mensagem do protocolo
     */
    fun toStrokeStartMessage(): com.slideink.protocol.StrokeStartMessage {
        val firstPoint = points.firstOrNull() ?: throw IllegalStateException("Stroke has no points")
        return com.slideink.protocol.StrokeStartMessage(
            strokeId = id,
            x = firstPoint.x,
            y = firstPoint.y,
            color = color,
            size = size,
            tool = tool.name.lowercase()
        )
    }
    
    fun toStrokePointMessage(): com.slideink.protocol.StrokePointMessage {
        return com.slideink.protocol.StrokePointMessage(
            strokeId = id,
            points = points
        )
    }
    
    fun toStrokeEndMessage(): com.slideink.protocol.StrokeEndMessage {
        return com.slideink.protocol.StrokeEndMessage(strokeId = id)
    }
}

/**
 * Ponto individual dentro de um traço
 */
data class StrokePoint(
    val x: Float,
    val y: Float,
    val pressure: Float = 1.0f,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Tipos de ferramenta de anotação
 */
enum class ToolType {
    PEN,
    ERASER,
    HIGHLIGHTER,
    LASER
}

/**
 * Configuração atual da caneta
 */
data class PenConfig(
    val tool: ToolType = ToolType.PEN,
    val color: Int = 0xFF000000.toInt(), // Preto
    val size: Float = 4f
) {
    companion object {
        val Default = PenConfig()
        
        val Highlighter = PenConfig(
            tool = ToolType.HIGHLIGHTER,
            color = 0x60F59E0B.toInt(), // Amarelo transparente
            size = 20f
        )
        
        val Eraser = PenConfig(
            tool = ToolType.ERASER,
            color = 0xFFFFFFFF.toInt(), // Branco (para "apagar")
            size = 30f
        )
    }
}

/**
 * Estado do canvas de anotação
 */
data class CanvasState(
    val strokes: List<Stroke> = emptyList(),
    val currentStroke: Stroke? = null,
    val activeTool: PenConfig = PenConfig.Default,
    val isDrawing: Boolean = false
) {
    /**
     * Adiciona ponto ao traço atual
     */
    fun addPoint(x: Float, y: Float, pressure: Float = 1.0f): CanvasState {
        val point = StrokePoint(x, y, pressure)
        
        return if (currentStroke == null) {
            // Inicia novo traço
            val newStroke = Stroke(
                id = "stroke_${System.currentTimeMillis()}",
                points = listOf(point),
                color = activeTool.color,
                size = activeTool.size,
                tool = activeTool.tool
            )
            copy(currentStroke = newStroke, isDrawing = true)
        } else {
            // Adiciona ponto ao traço atual
            copy(
                currentStroke = currentStroke.copy(
                    points = currentStroke.points + point
                )
            )
        }
    }
    
    /**
     * Finaliza traço atual
     */
    fun endStroke(): CanvasState {
        return currentStroke?.let { stroke ->
            copy(
                strokes = strokes + stroke,
                currentStroke = null,
                isDrawing = false
            )
        } ?: this
    }
    
    /**
     * Cancela traço atual
     */
    fun cancelStroke(): CanvasState {
        return copy(currentStroke = null, isDrawing = false)
    }
    
    /**
     * Limpa todas as anotações
     */
    fun clear(): CanvasState {
        return copy(strokes = emptyList(), currentStroke = null)
    }
    
    /**
     * Desfaz último traço
     */
    fun undo(): CanvasState {
        return if (strokes.isNotEmpty()) {
            copy(strokes = strokes.dropLast(1))
        } else {
            this
        }
    }
}

/**
 * Informações de página/slide
 */
data class PageInfo(
    val currentPage: Int,
    val totalPages: Int,
    val title: String? = null
) {
    val isValid: Boolean
        get() = currentPage in 1..totalPages
    
    val progress: Float
        get() = if (totalPages > 0) currentPage.toFloat() / totalPages else 0f
}

/**
 * Estado de conexão
 */
data class ConnectionState(
    val status: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val transport: String? = null,
    val latencyMs: Int? = null,
    val quality: Int? = null
) {
    val isConnected: Boolean
        get() = status == ConnectionStatus.CONNECTED
    
    val isConnecting: Boolean
        get() = status == ConnectionStatus.CONNECTING
    
    val hasError: Boolean
        get() = status == ConnectionStatus.ERROR
}

enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

/**
 * Estado geral da aplicação
 */
data class AppState(
    val connectionState: ConnectionState = ConnectionState(),
    val canvasState: CanvasState = CanvasState(),
    val pageInfo: PageInfo? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
