package nstv.sheep.maths

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val FullCircleAngleInRadians = Math.toRadians(360.0)

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
fun getCircumferencePointForAngle(
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
fun getCurveControlPoint(p1: Offset, p2: Offset, center: Offset): Offset {
    // 1. get initial line formula slope
    val m1 = p1.getSlopeTo(p2)

    // 2. get perpendicular line slope: m1*m2 = -1
    val m2 = -1 / m1

    // 3. get middle point
    val middlePoint = getMiddlePoint(p1, p2)

    val radius = p2.distanceToOffset(p1).div(2)

    var angle = atan(m2).toDouble()

    /**
     * The angle obtained starts in the x axis up, using it we get a fluff opening to the right,
     * but the perpendicular line cuts the helper circle in 2 spaces: in angle and in angle + 180°.
     * If the center of the helper circle is in the left from the center of the circle, it means
     * the fluff should curve to the left instead of from the right of the line between p1 and p2,
     * so we need to use the "opposite angle" to get the "opposite point", hence we add 180°(PI)
     * */
    if (middlePoint.x < center.x) {
        angle += PI
    }

    return getCircumferencePointForAngle(
        angleInRadians = angle,
        radius = radius,
        circleCenter = middlePoint
    )
}

fun getMiddlePoint(p1: Offset, p2: Offset): Offset {
    val x = (p2.x + p1.x).div(2)
    val y = (p2.y + p1.y).div(2)
    return Offset(x, y)
}

fun getRectTopLeftForDiagonal(lineStart: Offset, lineEnd: Offset) = Offset(
    x = if (lineStart.x < lineEnd.x) lineStart.x else lineEnd.x,
    y = if (lineStart.y < lineEnd.y) lineStart.y else lineEnd.y
)

/**
 * Good ol' Pythagoras h^2 = a^2 + b^2 => distance^2 = (x1 - x0)^2 + (y1 - y0)^2
 */
fun Offset.distanceToOffset(offset: Offset): Float =
    sqrt((offset.x - this.x).pow(2) + (offset.y - this.y).pow(2))

/**
 * line formula: y = mx + b where m is the slope and b is the yIntercept
 * => m = y/x = (y1-y0)/(x1-x0)
 */
fun Offset.getSlopeTo(offset: Offset): Float {
    return (offset.y - this.y).div(offset.x - this.x)
}
