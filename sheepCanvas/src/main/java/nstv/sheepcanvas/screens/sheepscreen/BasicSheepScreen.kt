package nstv.sheepcanvas.screens.sheepscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import nstv.canvasExtensions.distanceToOffset
import nstv.canvasExtensions.getCircumferencePointForAngle
import nstv.canvasExtensions.getMiddlePoint
import nstv.canvasExtensions.guidelines.GuidelineDashPattern
import nstv.canvasExtensions.guidelines.drawAxis
import nstv.canvasExtensions.guidelines.drawPoint
import nstv.canvasExtensions.guidelines.drawRectGuideline
import nstv.design.theme.ComposableSheepTheme

private const val ShowGuidelines = false

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
                drawPoint()
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

fun DrawScope.drawSimpleFluffCircles(
    color: Color,
    radius: Float,
    center: Offset = size.center,
    numberOfFluffs: Int = 10
) {
    val fullCircleAngleInRadians = Math.toRadians(360.0)
    val singleFluffAngle = fullCircleAngleInRadians.div(numberOfFluffs)

    var totalAngle = 0.0 // Previous angle

    var lastFluffEndOffset = getCircumferencePointForAngle(
        angleInRadians = 0.0,
        radius = radius,
        circleCenter = center
    )

    while (totalAngle < fullCircleAngleInRadians) {
        // 1. Get the next fluff end point
        val nextFluffTotalAngle = totalAngle + singleFluffAngle
        val nextFluffEndOffset = getCircumferencePointForAngle(
            angleInRadians = nextFluffTotalAngle,
            radius = radius,
            circleCenter = center
        )

        // 2. Get the radius of the fluff
        val fluffRadius = lastFluffEndOffset.distanceToOffset(nextFluffEndOffset).div(2)

        // 3. Get the middle point between the start and end of the current fluff
        val fluffCenter = getMiddlePoint(lastFluffEndOffset, nextFluffEndOffset)

        // 4. Build the fluff circle
        drawCircle(
            color = color,
            radius = fluffRadius,
            center = fluffCenter
        )

        // Update values to the next fluff
        totalAngle = nextFluffTotalAngle
        lastFluffEndOffset = nextFluffEndOffset

        if (ShowGuidelines) {
            // Guidelines
            drawLine(
                color = Color.Cyan,
                strokeWidth = 10f,
                start = center,
                end = nextFluffEndOffset
            )
            drawCircle(
                color = Color.Black,
                radius = fluffRadius,
                center = fluffCenter,
                style = Stroke(pathEffect = GuidelineDashPattern, width = 5f)
            )

            drawPoint(
                color = Color.Red,
                offset = nextFluffEndOffset,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ComposableSheepTheme {
        BasicSheepScreen()
    }
}
