package com.slideink.android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.slideink.android.domain.model.*

/**
 * Canvas de anotação para Jetpack Compose
 * 
 * Implementa a superfície de escrita conforme Documento Consolidado:
 * - Escrever
 * - Desenhar
 * - Destacar (highlighter)
 * - Apagar
 * - Limpar tela
 */
@Composable
fun InteractiveAnnotationCanvas(
    canvasState: CanvasState,
    onStrokeStart: (Float, Float) -> Unit,
    onStrokeMove: (Float, Float, Float) -> Unit,
    onStrokeEnd: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true
) {
    Canvas(
        modifier = modifier
            .pointerInput(canvasState) {
                if (!enabled) return@pointerInput
                
                detectDragGestures(
                    onDragStart = { offset ->
                        onStrokeStart(offset.x, offset.y)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onStrokeMove(
                            change.position.x,
                            change.position.y,
                            change.pressure
                        )
                    },
                    onDragEnd = {
                        onStrokeEnd()
                    },
                    onDragCancel = {
                        onStrokeEnd()
                    }
                )
            }
    ) {
        // Fundo
        drawRect(color = backgroundColor)
        
        // Desenha todas as strokes finalizadas
        canvasState.strokes.forEach { stroke ->
            drawStroke(stroke)
        }
        
        // Desenha stroke atual (em progresso)
        canvasState.currentStroke?.let { stroke ->
            drawStroke(stroke)
        }
    }
}

/**
 * Desenha um traço no canvas
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStroke(stroke: Stroke) {
    if (stroke.points.isEmpty()) return
    
    val color = Color(stroke.color)
    val strokeWidth = stroke.size
    
    when (stroke.tool) {
        ToolType.PEN -> {
            // Desenha linha suave conectando pontos
            if (stroke.points.size > 1) {
                val path = Path().apply {
                    moveTo(stroke.points[0].x, stroke.points[0].y)
                    for (i in 1 until stroke.points.size) {
                        lineTo(stroke.points[i].x, stroke.points[i].y)
                    }
                }
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            } else {
                // Ponto único
                drawCircle(
                    color = color,
                    radius = strokeWidth / 2,
                    center = Offset(stroke.points[0].x, stroke.points[0].y)
                )
            }
        }
        
        ToolType.HIGHLIGHTER -> {
            // Highlighter mais largo e semi-transparente
            if (stroke.points.size > 1) {
                val path = Path().apply {
                    moveTo(stroke.points[0].x, stroke.points[0].y)
                    for (i in 1 until stroke.points.size) {
                        lineTo(stroke.points[i].x, stroke.points[i].y)
                    }
                }
                drawPath(
                    path = path,
                    color = color.copy(alpha = 0.4f),
                    style = Stroke(
                        width = strokeWidth * 1.5f,
                        cap = StrokeCap.Butt,
                        join = StrokeJoin.Bevel
                    )
                )
            }
        }
        
        ToolType.ERASER -> {
            // Borracha usa blend mode para remover
            if (stroke.points.size > 1) {
                val path = Path().apply {
                    moveTo(stroke.points[0].x, stroke.points[0].y)
                    for (i in 1 until stroke.points.size) {
                        lineTo(stroke.points[i].x, stroke.points[i].y)
                    }
                }
                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(
                        width = strokeWidth * 2f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    blendMode = androidx.compose.ui.graphics.BlendMode.Clear
                )
            }
        }
        
        ToolType.LASER -> {
            // Laser não desenha no canvas Android
        }
    }
}
