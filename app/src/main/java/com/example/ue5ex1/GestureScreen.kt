package com.example.ue5ex1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun GestureScreen() {
    // Liste von Strichen, jeder Strich ist eine Liste von Punkten
    val strokes = remember { mutableStateListOf<List<Offset>>() }
    var currentStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var brushSize by remember { mutableStateOf(10f) }

    Column(Modifier.fillMaxSize()) {
        // Zeichenbereich
        Box(
            Modifier
                .weight(1f)  // Nimmt vertikal so viel Platz wie möglich
                .fillMaxWidth() // Nimmt 100 % Breite
                .pointerInput(Unit) {
                    // Drag zum Zeichnen
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentStroke = listOf(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentStroke = currentStroke + change.position
                        },
                        onDragEnd = {
                            strokes += currentStroke
                            currentStroke = emptyList()
                        },
                        onDragCancel = {
                            currentStroke = emptyList()
                        }
                    )
                }
                .pointerInput(Unit) {
                    // Long-Press zum Reset
                    detectTapGestures(
                        onLongPress = {
                            strokes.clear()
                            currentStroke = emptyList()
                        }
                    )
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                // bereits abgeschlossene Striche
                strokes.forEach { strokePoints ->
                    if (strokePoints.size > 1) {
                        val path = Path().apply {
                            moveTo(strokePoints.first().x, strokePoints.first().y)
                            strokePoints.drop(1).forEach { lineTo(it.x, it.y) }
                        }
                        drawPath(path, Color.Black, style = Stroke(width = brushSize, cap = StrokeCap.Round))
                    }
                }
                // aktueller Strich in Arbeit
                if (currentStroke.size > 1) {
                    val path = Path().apply {
                        moveTo(currentStroke.first().x, currentStroke.first().y)
                        currentStroke.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                    drawPath(path, Color.Black, style = Stroke(width = brushSize, cap = StrokeCap.Round))
                }
            }
        }

        // Pinsel-Größen-Slider
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Pinselgröße:", fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Slider(
                value = brushSize,
                onValueChange = { brushSize = it },
                valueRange = 1f..50f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GestureScreenPreview() {
    GestureScreen()
}