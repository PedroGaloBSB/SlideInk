package com.slideink.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slideink.android.domain.model.ToolType
import com.slideink.android.ui.theme.*

/**
 * Toolbar de ferramentas de anotação
 * 
 * Implementa conforme Documento Consolidado:
 * - Caneta
 * - Borracha
 * - Highlighter
 * - Laser
 * - Limpar
 * - Desfazer
 */
@Composable
fun AnnotationToolbar(
    currentTool: ToolType,
    currentColor: Int,
    currentSize: Float,
    onToolSelected: (ToolType) -> Unit,
    onColorSelected: (Int) -> Unit,
    onSizeChanged: (Float) -> Unit,
    onClear: () -> Unit,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Linha 1: Ferramentas principais
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Caneta
            ToolButton(
                icon = Icons.Default.Edit,
                label = "Caneta",
                isSelected = currentTool == ToolType.PEN,
                onClick = { onToolSelected(ToolType.PEN) }
            )
            
            // Highlighter
            ToolButton(
                icon = Icons.Default.Highlight,
                label = "Marcação",
                isSelected = currentTool == ToolType.HIGHLIGHTER,
                onClick = { onToolSelected(ToolType.HIGHLIGHTER) }
            )
            
            // Borracha
            ToolButton(
                icon = Icons.Default.Backspace,
                label = "Borracha",
                isSelected = currentTool == ToolType.ERASER,
                onClick = { onToolSelected(ToolType.ERASER) }
            )
            
            // Laser
            ToolButton(
                icon = Icons.Default.TrendingFlat,
                label = "Laser",
                isSelected = currentTool == ToolType.LASER,
                onClick = { onToolSelected(ToolType.LASER) }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Desfazer
            IconButton(
                onClick = onUndo,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Undo,
                    contentDescription = "Desfazer",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Limpar tudo
            IconButton(
                onClick = onClear,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Limpar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Linha 2: Cores e tamanho
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment =.Alignment.CenterVertically
        ) {
            // Seletor de cores
            ColorButton(
                color = Color.Black,
                isSelected = currentColor == Color.Black.hashCode(),
                onClick = { onColorSelected(Color.Black.hashCode()) }
            )
            
            ColorButton(
                color = Color.Red,
                isSelected = currentColor == Color.Red.hashCode(),
                onClick = { onColorSelected(Color.Red.hashCode()) }
            )
            
            ColorButton(
                color = Color.Blue,
                isSelected = currentColor == Color.Blue.hashCode(),
                onClick = { onColorSelected(Color.Blue.hashCode()) }
            )
            
            ColorButton(
                color = Color.Green,
                isSelected = currentColor == Color.Green.hashCode(),
                onClick = { onColorSelected(Color.Green.hashCode()) }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Slider de tamanho
            Text(
                text = "Tamanho:",
                style = MaterialTheme.typography.labelMedium
            )
            
            Slider(
                value = currentSize,
                onValueChange = onSizeChanged,
                valueRange = 2f..50f,
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Composable
private fun ToolButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Surface(
            shape = CircleShape,
            color = if (isSelected) {
                SlideInkPrimary.copy(alpha = 0.2f)
            } else {
                Color.Transparent
            },
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) SlideInkPrimary else Color.Gray,
                    shape = CircleShape
                )
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) {
                        SlideInkPrimary
                    } else {
                        Color.Gray
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                SlideInkPrimary
            } else {
                Color.Gray
            }
        )
    }
}

@Composable
private fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(32.dp)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, SlideInkPrimary, CircleShape)
                } else {
                    Modifier.border(1.dp, Color.LightGray, CircleShape)
                }
            )
            .background(color, CircleShape)
    )
}
