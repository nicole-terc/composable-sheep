package nstv.sheep.sheep

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

const val GuidelineStrokeWidth = 3f
val GuidelineDashPattern = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))

object GuidelineAlpha {
    const val strong: Float = 0.9f
    const val normal: Float = 0.5f
    const val low: Float = 0.2f
}

fun DrawScope.drawRectGuideline(
    topLeft: Offset,
    size: Size,
    degrees: Float = 0f,
    color: Color = Color.LightGray.copy(GuidelineAlpha.strong)
) {
    rotate(
        degrees = degrees,
        pivot = topLeft + size.center
    ) {
        drawRect(
            color = color,
            topLeft = topLeft,
            size = size,
            style = Stroke(
                width = GuidelineStrokeWidth,
                pathEffect = GuidelineDashPattern,
            )
        )
    }
}
