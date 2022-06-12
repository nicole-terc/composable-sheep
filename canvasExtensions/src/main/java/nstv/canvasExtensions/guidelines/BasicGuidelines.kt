package nstv.canvasExtensions.guidelines

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

const val GuidelineStrokeWidth = 5f
const val GridStrokeWidth = 2f
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
    strokeWidth: Float = GridStrokeWidth
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

fun DrawScope.drawAxis(
    color: Color = Color.DarkGray.copy(GuidelineAlpha.strong),
    colorX: Color = color,
    colorY: Color = color,
    pathEffect: PathEffect? = GuidelineDashPattern,
    lineStrokeWidth: Float = GuidelineStrokeWidth,
    axisCenter: Offset = size.center
) {
    // Horizontal Axis from Circle Center
    drawLine(
        color = colorX,
        strokeWidth = lineStrokeWidth,
        pathEffect = pathEffect,
        start = Offset(0f, axisCenter.y),
        end = Offset(size.width, axisCenter.y)
    )

    // Vertical Axis from Circle Center
    drawLine(
        color = colorY,
        strokeWidth = lineStrokeWidth,
        pathEffect = pathEffect,
        start = Offset(axisCenter.x, 0f),
        end = Offset(axisCenter.x, size.height)
    )

    drawPoint(color, lineStrokeWidth.times(4f), offset = axisCenter)
}

fun DrawScope.drawPoint(
    color: Color = Color.DarkGray.copy(GuidelineAlpha.strong),
    strokeWidth: Float = Grid.One.toPx(),
    offset: Offset = size.center
) {
    // Center point of Main Circle
    drawPoints(
        listOf(offset),
        color = color,
        pointMode = PointMode.Points,
        cap = StrokeCap.Round,
        strokeWidth = strokeWidth
    )
}

fun Size.getCenterOffset(topLeft: Offset) = topLeft + this.center
