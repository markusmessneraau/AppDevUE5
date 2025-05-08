package com.example.ue5ex1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun MultiTouchScreen() {
    val pointerPaths = remember { mutableStateMapOf<Int, MutableList<Offset>>() }
    val pointerColors = remember { mutableStateMapOf<Int, Color>() }
    val activePointers = remember { mutableStateMapOf<Int, Offset>() }

    Column(Modifier.fillMaxSize()) {
        Text(
            text = "Active fingers: ${activePointers.size}",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {//coroutine, für mehrere pointerevents
                        while (true) {      // solange coroutine aktiv
                            val event = awaitPointerEvent(PointerEventPass.Main)
                            event.changes.forEach { change -> //change = einzelne pointer events
                                val id = change.id.value.toInt()
                                val pos = change.position

                                if (change.pressed) {
                                    activePointers[id] = pos
                                    val path = pointerPaths.getOrPut(id) { mutableListOf() }
                                    path.add(pos)
                                    pointerColors.putIfAbsent(id, randomColor())
                                    change.consume() //als verarbeitet markieren
                                } else if (!change.pressed && change.previousPressed) {
                                    activePointers.remove(id)
                                    change.consume()
                                }
                            }
                        }
                    }
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                pointerPaths.forEach { (id, path) ->
                    val color = pointerColors[id] ?: Color.Black
                    if (path.size > 1) {
                        val drawPath = Path().apply {
                            moveTo(path.first().x, path.first().y)
                            path.drop(1).forEach { lineTo(it.x, it.y) }
                        }
                        drawPath(drawPath, color, style = Stroke(width = 6f, cap = StrokeCap.Round))
                    }
                }

                activePointers.forEach { (id, pos) ->
                    val color = pointerColors[id] ?: Color.Black
                    drawCircle(color, radius = 30f, center = pos)
                    drawContext.canvas.nativeCanvas.drawText(
                        "ID: $id",
                        pos.x + 35f,
                        pos.y,
                        android.graphics.Paint().apply {
                            setColor(android.graphics.Color.BLACK)
                            textSize = 40f
                        }
                    )
                }
            }
        }
    }
}

fun randomColor(): Color {
    return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
}
