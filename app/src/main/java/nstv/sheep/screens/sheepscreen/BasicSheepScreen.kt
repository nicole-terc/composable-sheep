package nstv.sheep.screens.sheepscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import nstv.design.theme.ComposableSheepAnimationsTheme
import nstv.sheep.guidelines.drawAxis
import nstv.sheep.guidelines.drawRectGuideline

private const val ShowGuidelines = true

@Composable
fun BasicSheepScreen() {

    // Sheep Colors
    val fluffColor = Color.LightGray
    val bodyColor = Color.DarkGray

    // Round sheep
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onDraw = {

            val bodyRadius = size.width.div(3f)

            // LEGS
            val legSize = Size(
                width = bodyRadius.div(4f),
                height = bodyRadius.times(1.2f)
            )

            val legSeparation = legSize.width.times(2f)

            val leftLegTopLeft = Offset(
                x = center.x - legSize.width - legSeparation.div(2),
                y = center.y
            )

            val rightLegTopLeft = Offset(
                x = center.x + legSeparation.div(2),
                y = center.y
            )

            // Left leg
            drawRect(
                color = bodyColor,
                topLeft = leftLegTopLeft,
                size = legSize
            )

            // Right leg
            drawRect(
                color = bodyColor,
                topLeft = rightLegTopLeft,
                size = legSize
            )

            // FLUFF
            drawCircle(
                color = fluffColor,
                center = center,
                radius = bodyRadius
            )

            // HEAD

            // Head size is as width as half the body (circle radius) and has a 2/3 height ratio
            val headSize = Size(
                width = bodyRadius,
                height = bodyRadius.times(2f / 3f)
            )

            // Head is 1/3 out of the fluff and 1/4 above the center of the circle
            val headTopLeft = Offset(
                x = center.x - bodyRadius - headSize.width.div(3f),
                y = center.y - headSize.height.div(4f)
            )

            drawOval(
                color = bodyColor,
                topLeft = headTopLeft,
                size = headSize
            )

            if (ShowGuidelines) {
                drawAxis(
                    colorY = Color.Blue,
                    colorX = Color.Red
                )
                drawRectGuideline(
                    topLeft = headTopLeft,
                    size = headSize,
                    color = Color.Green
                )
                drawRectGuideline(
                    topLeft = leftLegTopLeft,
                    size = legSize,
                    color = Color.Magenta
                )
                drawRectGuideline(
                    topLeft = rightLegTopLeft,
                    size = legSize,
                    color = Color.Cyan
                )
            }
        }
    )
}

fun DrawScope.drawBasicFluffCircle(
    color: Color,
    bodyRadius: Float
) {
    drawCircle(
        color = color,
        center = center,
        radius = bodyRadius
    )
}

fun DrawScope.drawBasicHead(
    color: Color,
    bodyRadius: Float
) {
    // HEAD
    // Head size is as width as half the body (circle radius) and has a 2/3 height ratio
    val headSize = Size(
        width = bodyRadius,
        height = bodyRadius.times(2f / 3f)
    )

    // Head is 1/3 out of the fluff and 1/4 above the center of the circle
    val headTopLeft = Offset(
        x = center.x - bodyRadius - headSize.width.div(3f),
        y = center.y - headSize.height.div(4f)
    )

    drawOval(
        color = color,
        topLeft = headTopLeft,
        size = headSize
    )
}

fun DrawScope.drawBasicLegs(
    color: Color,
    bodyRadius: Float
) {
    // LEGS
    val legSize = Size(
        width = bodyRadius.div(4f),
        height = bodyRadius.times(1.2f)
    )

    val legSeparation = legSize.width.times(2f)

    val leftLegTopLeft = Offset(
        x = center.x - legSize.width - legSeparation.div(2),
        y = center.y
    )

    val rightLegTopLeft = Offset(
        x = center.x + legSeparation.div(2),
        y = center.y
    )

    // Left leg
    drawRect(
        color = color,
        topLeft = leftLegTopLeft,
        size = legSize
    )

    // Right leg
    drawRect(
        color = color,
        topLeft = rightLegTopLeft,
        size = legSize
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ComposableSheepAnimationsTheme {
        BasicSheepScreen()
    }
}
