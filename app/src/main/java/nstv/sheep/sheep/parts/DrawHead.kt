package nstv.sheep.sheep.parts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import nstv.sheep.sheep.drawRectGuideline
import nstv.sheep.sheep.model.Sheep

fun DrawScope.drawHead(
    circleCenterOffset: Offset,
    circleRadius: Float,
    sheep: Sheep,
    showGuidelines: Boolean = false
) {

    // head aspect ration 1:2/3 => height = 0.66 * headWidth = 0.66 * radius
    val headSize = Size(
        width = circleRadius,
        height = circleRadius.times(2f / 3f)
    )

    // Head is 1/3 out of the fluff and 1/4 above the center of the circle
    val headTopLeft = Offset(
        x = circleCenterOffset.x - circleRadius - headSize.width.div(3f),
        y = circleCenterOffset.y - headSize.height.div(4f)
    )

    val headAngle = sheep.headAngle
    val headCenter = headTopLeft + headSize.center

    // Head
    rotate(
        degrees = headAngle,
        pivot = headCenter
    ) {
        drawArc(
            color = Color.DarkGray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            topLeft = headTopLeft,
            size = headSize,
        )
    }

    if (showGuidelines) {
        drawRectGuideline(
            topLeft = headTopLeft,
            size = headSize,
            degrees = headAngle
        )
    }

    drawGlasses(
        headTopLeft = headTopLeft,
        headSize = headSize,
        headAngle = headAngle,
        headCenter = headCenter,
        showGuidelines = showGuidelines
    )
}
