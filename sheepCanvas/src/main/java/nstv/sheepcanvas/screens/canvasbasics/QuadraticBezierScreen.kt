package nstv.sheepcanvas.screens.canvasbasics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.canvasExtensions.guidelines.GuidelineDashPattern
import nstv.canvasExtensions.guidelines.drawGrid
import nstv.canvasExtensions.guidelines.drawPoint
import nstv.canvasExtensions.maths.getCurveControlPoint
import nstv.design.theme.ComposableSheepTheme

@Composable
fun QuadraticBezierScreen() {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawGrid()

            val distance = 100.dp.toPx()
            val pointOne = Offset(center.x.minus(distance), center.y)
            val pointTwo = Offset(center.x.plus(distance), center.y)

            val controlPoint = getCurveControlPoint(pointOne, pointTwo, center)
            val controlPoint2 = Offset(x = controlPoint.x, y = controlPoint.y.times(0.7f))
            val controlPoint3 = Offset(x = pointOne.x.times(1.2f), y = controlPoint.y.times(1.1f))

            translate(top = -center.y.times(0.6f)) {
                drawQuadraticPathWithGuideline(pointOne, pointTwo, controlPoint)
            }

            drawQuadraticPathWithGuideline(pointOne, pointTwo, controlPoint2)

            translate(top = center.y.times(0.4f)) {
                drawQuadraticPathWithGuideline(pointOne, pointTwo, controlPoint3)
            }
        }
    )
}

private fun DrawScope.drawQuadraticPathWithGuideline(
    pointOne: Offset,
    pointTwo: Offset,
    controlPoint: Offset
) {
    val lineStrokeWidth = 4.dp.toPx()
    val lineStrokeWidthGuideline = 2.dp.toPx()

    val path = Path().apply {
        moveTo(pointOne.x, pointOne.y)
        quadraticBezierTo(controlPoint.x, controlPoint.y, pointTwo.x, pointTwo.y)
    }

    drawPath(path, Color.Black, style = Stroke(lineStrokeWidth))

    drawLine(
        color = Color.LightGray,
        start = pointOne,
        end = controlPoint,
        strokeWidth = lineStrokeWidthGuideline,
        pathEffect = GuidelineDashPattern
    )

    drawLine(
        color = Color.LightGray,
        start = pointTwo,
        end = controlPoint,
        strokeWidth = lineStrokeWidthGuideline,
        pathEffect = GuidelineDashPattern
    )

    drawPoint(color = Color.Blue, offset = pointOne)
    drawPoint(color = Color.Blue, offset = pointTwo)

    drawPoint(color = Color.Red, offset = controlPoint)
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ComposableSheepTheme {
        QuadraticBezierScreen()
    }
}
