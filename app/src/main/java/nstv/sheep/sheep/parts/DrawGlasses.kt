package nstv.sheep.sheep.parts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import nstv.sheep.sheep.GuidelineAlpha
import nstv.sheep.sheep.drawRectGuideline

fun DrawScope.drawGlasses(
    headTopLeft: Offset,
    headSize: Size,
    headAngle: Float,
    headCenter: Offset,
    singleGlassHeadRatio: Float = 0.45f,
    glassOverlapPercentage: Float = 0.25f,
    glassOffsetFromHeadCenter: Float = 0.9f,
    glassSeparationPercentage: Float = 0.1f,
    showGuidelines: Boolean = false
) {
    val glassWidth = headSize.width.times(singleGlassHeadRatio)

    /**
     * Each glass will be a semicircle with 2/1 ratio.
     * Reference rect for drawArc contains the full circumference
     * => reference rect is a square to draw the bottom half of the circle
     */
    val glassSize = Size(
        width = glassWidth,
        height = glassWidth
    )

    val leftGlassTopLeft = Offset(
        x = headCenter.x - glassSize.width.times(glassOffsetFromHeadCenter),
        y = headTopLeft.y - glassSize.height.times(1f - glassOverlapPercentage)
    )

    val rightGlassTopLeft = Offset(
        x = leftGlassTopLeft.x + glassSize.width.times(1f + glassSeparationPercentage),
        y = leftGlassTopLeft.y
    )

    val glassAngle = headAngle + 10f

    rotate(
        degrees = glassAngle,
        pivot = headCenter
    ) {

        // Left glass
        drawArc(
            color = Color.Black,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = leftGlassTopLeft,
            size = glassSize,
        )

        // Right glass
        drawArc(
            color = Color.Black,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = rightGlassTopLeft,
            size = glassSize,
        )

        // Bridge
        drawLine(
            color = Color.Black,
            start = leftGlassTopLeft + glassSize.center.times(1.4f),
            end = rightGlassTopLeft + glassSize.center.times(1.4f),
            strokeWidth = 6f
        )
    }

    if (showGuidelines) {
        drawGlassGuidelines(
            topLeft = leftGlassTopLeft,
            size = glassSize,
            degrees = glassAngle
        )

        drawGlassGuidelines(
            topLeft = rightGlassTopLeft,
            size = glassSize,
            degrees = glassAngle
        )
    }
}

private fun DrawScope.drawGlassGuidelines(
    topLeft: Offset,
    size: Size,
    degrees: Float
) {
    drawRectGuideline(
        topLeft = topLeft,
        size = size,
        degrees = degrees
    )

    drawCircle(
        color = Color.Magenta.copy(GuidelineAlpha.normal),
        radius = size.width.div(2f),
        center = topLeft + size.center
    )
}
