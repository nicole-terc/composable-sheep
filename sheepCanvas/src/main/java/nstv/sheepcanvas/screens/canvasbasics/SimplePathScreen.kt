package nstv.sheepcanvas.screens.canvasbasics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.canvasExtensions.guidelines.drawAxis
import nstv.canvasExtensions.guidelines.drawGrid
import nstv.canvasExtensions.guidelines.drawPoint
import nstv.canvasExtensions.maths.getCurveControlPoint
import nstv.design.theme.ComposableSheepTheme

@Composable
fun SimplePathScreen() {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawGrid()
            drawAxis()

            val path = Path().apply {
                // Moves without drawing
                moveTo(x = center.x, y = center.y)

                // Draw Lines
                lineTo(x = center.x / 2, y = center.y)
                lineTo(x = center.x / 2, y = center.y * 1.5f)
                lineTo(x = center.x, y = center.y * 1.5f)

//                // Draw Simple Arc
//                arcTo(
//                    rect = Rect(
//                        left = center.x / 2,
//                        top = center.y,
//                        right = center.x * 1.5f,
//                        bottom = center.y * 1.5f
//                    ),
//                    startAngleDegrees = 90f,
//                    sweepAngleDegrees = -180f,
//                    forceMoveTo = true
//                )

                // Draw Quadratic Bezier Curve
                val controlPoint = getCurveControlPoint(
                    p1 = Offset(center.x, y = center.y * 1.5f),
                    p2 = center,
                    center = center.copy(y = center.y * 1.25f)
                )

                quadraticBezierTo(
                    x1 = controlPoint.x,
                    y1 = controlPoint.y,
                    x2 = center.x,
                    y2 = center.y
                )
            }

            drawPath(
                path = path,
                color = Color.Red,
                style = Stroke(width = 4.dp.toPx()),
            )

            drawPoint()
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ComposableSheepTheme {
        SimplePathScreen()
    }
}
