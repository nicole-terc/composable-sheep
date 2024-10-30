package nstv.sheepcanvas.screens.sheepscreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.canvasExtensions.guidelines.GuidelineDashPattern
import nstv.canvasExtensions.guidelines.drawAxis
import nstv.canvasExtensions.guidelines.drawPoint
import nstv.canvasExtensions.guidelines.drawRectGuideline
import nstv.canvasExtensions.maths.FullCircleAngleInRadians
import nstv.canvasExtensions.maths.distanceToOffset
import nstv.canvasExtensions.maths.getCircumferencePointForAngle
import nstv.canvasExtensions.maths.getCurveControlPoint
import nstv.canvasExtensions.maths.getMiddlePoint
import nstv.canvasExtensions.maths.toRadians
import nstv.sheepcanvas.screens.sheepscreen.BasicSheepColor.Fluff
import nstv.sheepcanvas.screens.sheepscreen.BasicSheepColor.Skin

/**
 * Have fun with the Basic Composable Sheep!
 * Found more about them here: https://github.com/nicole-terc/composable-sheep
 */

private const val ShowGuidelines = false

object BasicSheepColor {
    val Gray = Color(0xFFCCCCCC)
    val Blue = Color(0xFF1976D2)
    val Green = Color(0xFF3DDC84)
    val Purple = Color(0xFF6200EA)
    val Magenta = Color(0xFFC51162)
    val Orange = Color(0xFFFF9800)
    val Red = Color(0xFFFF0000)
    val Fluff = Color(0xFFCCCCCC)
    val Skin = Color(0xFF444444)

    val list = listOf(
        Gray,
        Blue,
        Green,
        Purple,
        Magenta,
        Orange,
        Red,
        Fluff,
    )
}

@Composable
fun LoadingBasicSheep(
    modifier: Modifier = Modifier,
    spinning: Boolean = true,
) {
    val durationMillis = 1000
    val delayMillis = 300
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier) {
        Box(Modifier.fillMaxSize()) {
            BasicSheep(
                modifier = Modifier
                    .fillMaxSize(.5f)
                    .aspectRatio(
                        1f,
                        matchHeightConstraintsFirst = true
                    )
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        if (spinning) {
                            transformOrigin = TransformOrigin(
                                pivotFractionX = 0.5f,
                                pivotFractionY = 0.1f,
                            )
                            rotationZ = rotation
                            scaleX = animatedScale
                            scaleY = animatedScale
                        }
                    },
            )
        }
    }
}

@Composable
fun BasicSheep(
    modifier: Modifier = Modifier,
) {
    // Basic sheep
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onDraw = {

            val bodyRadius = size.width.div(3f)

            // LEGS
            val legSize = Size(
                width = bodyRadius.div(4f),
                height = bodyRadius.times(1.2f)
            )

            val (rightLegTopLeft, leftLegTopLeft) = getSimpleLegsTopLeft(legSize)

            // Left leg
            drawRect(
                color = Skin,
                topLeft = leftLegTopLeft,
                size = legSize
            )

            // Right leg
            drawRect(
                color = Skin,
                topLeft = rightLegTopLeft,
                size = legSize
            )

            // FLUFF
            drawCircle(
                color = Fluff,
                center = center,
                radius = bodyRadius
            )

            // Basic fluff sheep
//            drawSimpleFluffCircles(
//                color = Fluff,
//                radius = bodyRadius
//            )

            // Bassic Sheep Path
            drawPath(
                getBasicFluffPath(
                    circleRadius = bodyRadius,
                    circleCenterOffset = center,
                ),
                brush = SolidColor(BasicSheepColor.list.random())
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
                color = Skin,
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

private fun DrawScope.drawSimpleFluffCircles(
    color: Color,
    radius: Float,
    center: Offset = size.center,
    numberOfFluffs: Int = 15
) {
    val singleFluffAngle = FullCircleAngleInRadians.div(numberOfFluffs)

    var totalAngle = 0.0 // Previous angle

    var lastFluffEndOffset = getCircumferencePointForAngle(
        angleInRadians = 0.0,
        radius = radius,
        circleCenter = center
    )

    while (totalAngle < FullCircleAngleInRadians) {
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

private fun DrawScope.getSimpleLegsTopLeft(
    legSize: Size,
): Pair<Offset, Offset> {
    // LEGS
    val legSeparation = legSize.width.times(2f)

    val leftLegTopLeft = Offset(
        x = center.x - legSize.width - legSeparation.div(2),
        y = center.y
    )

    val rightLegTopLeft = Offset(
        x = center.x + legSeparation.div(2),
        y = center.y
    )

    return Pair(leftLegTopLeft, rightLegTopLeft)
}

fun getBasicFluffPath(
    circleRadius: Float,
    circleCenterOffset: Offset,
    numberOfFluffChunks: Int = 10,
) = getFluffPath(
    fluffPoints = getBasicFluffPoints(
        fluffPercentages = List(numberOfFluffChunks) { 100.0.div(numberOfFluffChunks) },
        radius = circleRadius,
        circleCenter = circleCenterOffset
    ),
    circleRadius = circleRadius,
    circleCenterOffset = circleCenterOffset,
)

private fun getBasicFluffPoints(
    fluffPercentages: List<Double>,
    radius: Float,
    circleCenter: Offset = Offset.Zero,
    totalAngleInRadians: Double = FullCircleAngleInRadians
): List<Offset> {
    val fluffPoints = mutableListOf<Offset>()

    var totalPercentage = 0.0
    fluffPercentages.forEach { fluffPercentage ->
        totalPercentage += fluffPercentage
        fluffPoints.add(
            getCircumferencePointForAngle(
                totalPercentage.div(100.0).times(totalAngleInRadians),
                radius,
                circleCenter
            )
        )
    }
    return fluffPoints
}

/**
 * Returns the path of the fluff for the given fluff points.
 * Uses quadratic brazier curves to create the fluff curves.
 */

fun getFluffPath(
    fluffPoints: List<Offset>,
    circleRadius: Float,
    circleCenterOffset: Offset,
) = Path().apply {
    var currentPoint = getCircumferencePointForAngle(
        0.0.toRadians(),
        circleRadius,
        circleCenterOffset
    )

    moveTo(currentPoint.x, currentPoint.y)

    fluffPoints.forEach { fluffPoint ->
        val controlPoint =
            getCurveControlPoint(currentPoint, fluffPoint, circleCenterOffset)
        quadraticBezierTo(controlPoint.x, controlPoint.y, fluffPoint.x, fluffPoint.y)
        currentPoint = fluffPoint
    }
}

@Preview
@Composable
private fun PreviewLoadingSheep() {
    LoadingBasicSheep(Modifier.size(200.dp))
}

@Preview
@Composable
private fun PreviewBasicSheep() {
    BasicSheep()
}