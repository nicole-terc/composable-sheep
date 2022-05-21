package nstv.sheep.sheep

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.design.theme.ComposableSheepAnimationsTheme
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val FullCircleAngleInRadians = Math.toRadians(360.0)

@Composable
fun SheepComposable(
    sheep: Sheep,
    modifier: Modifier = Modifier,
    showGuidelines: Boolean = false,
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val circleRadius = size.width * 0.4f
        val circleCenterOffset = Offset(canvasWidth / 2f, canvasHeight / 2f)

        val fluffPoints =
            getFluffPoints(
                sheep = sheep,
                radius = circleRadius,
                circleCenter = circleCenterOffset
            )

        val fluffPath = Path().apply {
            var currentPoint =
                getCircumferencePointForAngle(
                    Math.toRadians(0.0),
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
            close()
        }

        drawPath(path = fluffPath, color = Color.Blue.copy(alpha = 0.7f))

        if (showGuidelines) {
            drawCanvasGuidelines(
                drawScope = this,
                canvasHeight = canvasHeight,
                canvasWidth = canvasWidth,
                circleCenterOffset = circleCenterOffset,
                circleRadius = circleRadius,
                fluffPoints = fluffPoints
            )
        }
    }
}

private fun drawCanvasGuidelines(
    drawScope: DrawScope,
    canvasHeight: Float,
    canvasWidth: Float,
    circleCenterOffset: Offset,
    circleRadius: Float,
    fluffPoints: List<Offset>,
) {
    with(drawScope) {
        // Base Circle
        drawCircle(
            color = Color.Black.copy(alpha = 0.8f),
            center = center,
            radius = circleRadius
        )

        // Vertical Axis from Circle Center
        drawLine(
            color = Color.LightGray,
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)),
            start = Offset(circleCenterOffset.x, 0f),
            end = Offset(circleCenterOffset.x, canvasHeight)
        )

        // Horizontal Axis from Circle Center
        drawLine(
            color = Color.LightGray,
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)),
            start = Offset(0f, circleCenterOffset.y),
            end = Offset(canvasWidth, circleCenterOffset.y)
        )

        // Current fluff point in circumference
        var currentPointGuidelines =
            getCircumferencePointForAngle(Math.toRadians(0.0), circleRadius, circleCenterOffset)

        // Coordinates of middle points between 2 fluff points
        val midPoints: MutableList<Offset> = mutableListOf()

        fluffPoints.forEach { fluffPoint ->
            val middlePoint = getMiddlePoint(currentPointGuidelines, fluffPoint)

            midPoints.add(middlePoint)

            // Circles used to obtain reference point for Quadratic Bezier Curve
            drawCircle(
                color = Color.Cyan.copy(alpha = 0.5f),
                radius = middlePoint.distanceToOffset(fluffPoint).div(2),
                center = middlePoint
            )

            // Lines between 2 fluff points
            drawLine(
                color = Color.Red.copy(alpha = 0.9f),
                start = currentPointGuidelines,
                end = fluffPoint
            )

            currentPointGuidelines = fluffPoint
        }

        // Center point of Main Circle
        drawPoints(
            mutableListOf(circleCenterOffset),
            color = Color.White,
            pointMode = PointMode.Points,
            cap = StrokeCap.Round,
            strokeWidth = 16.dp.toPx()
        )

        // Fluff points (start/end of fluff curves)
        drawPoints(
            fluffPoints,
            color = Color.Red,
            pointMode = PointMode.Points,
            cap = StrokeCap.Round,
            strokeWidth = 8.dp.toPx()
        )

        // Mid points between 2 fluff points
        drawPoints(
            midPoints,
            color = Color.Yellow.copy(alpha = 0.2f),
            pointMode = PointMode.Points,
            cap = StrokeCap.Butt,
            strokeWidth = 8.dp.toPx()
        )
    }
}

private fun getFluffPoints(
    sheep: Sheep,
    radius: Float,
    circleCenter: Offset = Offset.Zero,
    totalAngleInRadians: Double = FullCircleAngleInRadians
): List<Offset> {
    val fluffPoints = mutableListOf<Offset>()
    var totalPercentage = 0.0
    sheep.fluffChunksPercentages.forEach { fluffPercentage ->
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
 * Returns a point in a circumference given the angle from the x axis in radians,
 * the circle radius (r) and the circle center (x0, y0)
 *
 * Crazy maths:
 * - x obtained with trigonometry: cos(angle) = Adjacent/Hypotenuse = (x-x0)/radius
 * => x = cos(angle) * r + x0
 *
 * - y obtained from circle formula: r^2 = (x-x0)^2 + (y-y0)^2
 * => y = sqrt[r^2 - (x-x0)^2] + y0
 * y sign obtained form sin(angle), same +/- sign as sin(angle)
 *
 **/
private fun getCircumferencePointForAngle(
    angleInRadians: Double,
    radius: Float,
    circleCenter: Offset = Offset.Zero
): Offset {
    // 1. get X
    val x = cos(angleInRadians).times(radius).plus(circleCenter.x)

    // 2. get Y sign
    val ySign = if (sin(angleInRadians) >= 0) 1 else -1
    val y = sqrt(radius.pow(2) - (x - circleCenter.x).pow(2)).times(ySign).plus(circleCenter.y)

    return Offset(x.toFloat(), y.toFloat())
}

/**
 * Objective:
 * Get the control point for the Quadratic Bezier Curve.
 * The control point is perpendicular to the line between p1&p2, passing through the middle of it
 * and at half the distance between p1&p2
 *
 * Strategy:
 * Get the formula of the line between p1&p2 (L1)
 * Get the perpendicular line (L2) of L1, we only need the slope
 * Use the perpendicular line (L2) slope to get the angle of L2 from the x axis
 * A circle can be formed from the center of L1 with a radius of half the distance of p1&p2
 * Use this circle and the angle of L2 to get the control point at half the distance of L1, from the
 * middle point of L1, using getCircumferencePointForAngle()
 *
 * Using:
 * - line formula: y = mx + b where m is the slope and b is the yIntercept
 * - perpendicular line formula slope is the negative reciprocal: m1 * m2 = -1 => m2 = -1/m1
 * - slope relation to angle: m = (y-y0)/(x-x0) = tan(angle) => angle = tan^-1(m)
 *
 */
private fun getCurveControlPoint(p1: Offset, p2: Offset, center: Offset): Offset {
    // 1. get initial line formula slope
    val m1 = (p2.y - p1.y).div(p2.x - p1.x)

    // 2. get perpendicular line formula slope: m1*m2 = -1
    val m2 = -1 / m1

    // 3. get middle point
    val middlePoint = getMiddlePoint(p1, p2)

    val radius = p2.distanceToOffset(p1).div(2)

    var angle = atan(m2).toDouble()

    if (middlePoint.x < center.x) {
        angle += PI
    }

    return getCircumferencePointForAngle(
        angleInRadians = angle,
        radius = radius,
        circleCenter = middlePoint
    )
}

private fun getMiddlePoint(p1: Offset, p2: Offset): Offset {
    val x = (p2.x - p1.x).div(2).plus(p1.x)
    val y = (p2.y - p1.y).div(2).plus(p1.y)
    return Offset(x, y)
}

/**
 * Good ol' Pythagoras h^2 = a^2 + b^2 => distance^2 = (x1 - x0)^2 + (y1 - y0)^2
 */
fun Offset.distanceToOffset(offset: Offset): Float =
    sqrt((offset.x - this.x).pow(2) + (offset.y - this.y).pow(2))

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheep() {
    ComposableSheepAnimationsTheme {
        SheepComposable(modifier = Modifier.size(300.dp), sheep = Sheep(FluffStyle.Random()))
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheepGuidelines() {
    ComposableSheepAnimationsTheme {
        SheepComposable(
            modifier = Modifier.size(300.dp),
            sheep = Sheep(FluffStyle.Random()),
            showGuidelines = true
        )
    }
}
