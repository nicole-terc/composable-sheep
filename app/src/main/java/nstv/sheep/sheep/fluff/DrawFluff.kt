package nstv.sheep.sheep.fluff

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import nstv.sheep.sheep.FullCircleAngleInRadians
import nstv.sheep.sheep.Sheep
import nstv.sheep.sheep.distanceToOffset
import nstv.sheep.sheep.getCircumferencePointForAngle
import nstv.sheep.sheep.getCurveControlPoint
import nstv.sheep.sheep.getMiddlePoint

fun DrawScope.drawFluff(
    circleCenterOffset: Offset,
    circleRadius: Float,
    sheep: Sheep,
    showGuidelines: Boolean = false
) {

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
        drawFluffGuidelines(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            fluffPoints = fluffPoints
        )
    }
}

fun DrawScope.drawFluffGuidelines(
    circleCenterOffset: Offset,
    circleRadius: Float,
    fluffPoints: List<Offset>,
) {
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
        end = Offset(circleCenterOffset.x, size.height)
    )

    // Horizontal Axis from Circle Center
    drawLine(
        color = Color.LightGray,
        strokeWidth = 3f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)),
        start = Offset(0f, circleCenterOffset.y),
        end = Offset(size.width, circleCenterOffset.y)
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
