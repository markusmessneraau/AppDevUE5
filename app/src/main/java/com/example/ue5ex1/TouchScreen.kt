package com.example.ue5ex1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.hypot
import kotlin.random.Random
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

@Composable
fun TouchScreen() {
    //State-Halter für Größe, Positionen, Distanz
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var redCenter by remember { mutableStateOf(Offset.Zero) }
    var blueCenter by remember { mutableStateOf<Offset?>(null) }
    var distance by remember { mutableStateOf(0f) }

    //Radius definieren (50 dp → px)
    val radiusDp = 50.dp
    val radiusPx = with(LocalDensity.current) { radiusDp.toPx() }

    //roten Kreis random platzieren
    LaunchedEffect(canvasSize) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            val minX = radiusPx //abstand links
            val maxX = canvasSize.width - radiusPx//abstand rechts
            val minY = radiusPx//abstand oben
            val maxY = canvasSize.height - radiusPx//abstand unten

            redCenter = Offset(
                x = Random.nextFloat() * (maxX - minX) + minX,
                y = Random.nextFloat() * (maxY - minY) + minY
            )
        }
    }


    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tap ->
                        // Einfacher Tap: blauer Kreis + Abstand
                        blueCenter = tap
                        distance = hypot(tap.x - redCenter.x, tap.y - redCenter.y)
                    },
                    onDoubleTap = {
                        // Doppeltippen: roter Kreis zufällig neu platzieren
                        if (canvasSize.width > 0 && canvasSize.height > 0) {
                            redCenter = Offset(
                                x = Random.nextFloat() * (canvasSize.width - 2 * radiusPx) + radiusPx,
                                y = Random.nextFloat() * (canvasSize.height - 2 * radiusPx) + radiusPx
                            )
                            if(blueCenter != null) {
                                distance = hypot(blueCenter!!.x - redCenter.x, blueCenter!!.y - redCenter.y)
                            }

                        }
                    },
                    onLongPress = { longPress ->

                        redCenter = longPress

                        if(blueCenter != null) {
                            distance = hypot(blueCenter!!.x - redCenter.x, blueCenter!!.y - redCenter.y)
                        }
                    }
                )
            }
    ) {
        //Canvas zeichnet Kreise + Linie
        Canvas(Modifier.fillMaxSize()) {
            // Roter Kreis
            drawCircle(
                color = Color.Red,
                center = redCenter,
                radius = radiusPx
            )
            // Blauer Kreis + Linie, wenn getoucht
            blueCenter?.let { b ->  //wennn blueCenter einen Wert hat, block ausführen
                drawCircle(
                    color = Color.Blue,
                    center = b,
                    radius = radiusPx
                )
                drawLine(
                    color = Color.Black,
                    start = redCenter,
                    end = b,
                    strokeWidth = 5f
                )
            }
        }

        // Abstandstext als Overlay
        blueCenter?.let {
            Text(
                text = "Distance: ${"%.1f".format(distance)} px",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TouchScreenPreview() {
    TouchScreen()
}
