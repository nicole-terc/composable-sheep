package nstv.sheep.guidelines

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import nstv.design.theme.Grid

const val GuidelineStrokeWidth = 3f
val GuidelineDashPattern = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))

object GuidelineAlpha {
    const val strong: Float = 0.8f
    const val normal: Float = 0.4f
    const val low: Float = 0.2f
}

fun DrawScope.drawRectGuideline(
    topLeft: Offset,
    size: Size,
    degrees: Float = 0f,
    color: Color = Color.LightGray.copy(GuidelineAlpha.strong),
    stroke: Stroke = Stroke(
        width = GuidelineStrokeWidth,
        pathEffect = GuidelineDashPattern,
    )
) {
    rotate(
        degrees = degrees,
        pivot = topLeft + size.center
    ) {
        drawRect(
            color = color,
            topLeft = topLeft,
            size = size,
            style = stroke
        )
    }
}

fun DrawScope.drawGrid(
    numberOfCells: Int = 20,
    color: Color = Color.LightGray.copy(GuidelineAlpha.normal),
    pathEffect: PathEffect = GuidelineDashPattern,
    strokeWidth: Float = 2f
) {
    val stepSize = size.minDimension.div(numberOfCells)
    var currentStepPosition = 0f

    while (currentStepPosition <= size.maxDimension) {

        if (currentStepPosition <= size.height) {
            // x axis
            drawLine(
                color = color,
                start = Offset(x = 0f, y = currentStepPosition),
                end = Offset(x = size.width, y = currentStepPosition),
                pathEffect = pathEffect,
                strokeWidth = strokeWidth
            )
        }

        if (currentStepPosition <= size.width) {
            // y axis
            drawLine(
                color = color,
                start = Offset(x = currentStepPosition, y = 0f),
                end = Offset(x = currentStepPosition, y = size.height),
                pathEffect = pathEffect,
                strokeWidth = strokeWidth
            )
        }

        currentStepPosition += stepSize
    }
}

fun DrawScope.drawCenterAxis(
    color: Color = Color.DarkGray.copy(GuidelineAlpha.strong),
    pathEffect: PathEffect = GuidelineDashPattern,
    lineStrokeWidth: Float = GuidelineStrokeWidth,
) {

    // Vertical Axis from Circle Center
    drawLine(
        color = color,
        strokeWidth = lineStrokeWidth,
        pathEffect = pathEffect,
        start = Offset(size.center.x, 0f),
        end = Offset(size.center.x, size.height)
    )

    // Horizontal Axis from Circle Center
    drawLine(
        color = color,
        strokeWidth = lineStrokeWidth,
        pathEffect = pathEffect,
        start = Offset(0f, size.center.y),
        end = Offset(size.width, size.center.y)
    )

    drawCenterPoint(color)
}

fun DrawScope.drawCenterPoint(
    color: Color = Color.DarkGray.copy(GuidelineAlpha.strong),
    strokeWidth: Float = Grid.One.toPx(),
) {
    // Center point of Main Circle
    drawPoints(
        listOf(size.center),
        color = color,
        pointMode = PointMode.Points,
        cap = StrokeCap.Round,
        strokeWidth = strokeWidth
    )
}
