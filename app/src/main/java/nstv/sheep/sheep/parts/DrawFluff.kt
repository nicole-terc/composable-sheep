package nstv.sheep.sheep.parts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import nstv.sheep.guidelines.GuidelineAlpha
import nstv.sheep.guidelines.drawCenterAxis
import nstv.sheep.guidelines.drawGrid
import nstv.sheep.maths.FullCircleAngleInRadians
import nstv.sheep.maths.distanceToOffset
import nstv.sheep.maths.getCircumferencePointForAngle
import nstv.sheep.maths.getCurveControlPoint
import nstv.sheep.maths.getMiddlePoint
import nstv.sheep.sheep.model.Sheep

fun DrawScope.drawFluff(
    circleCenterOffset: Offset,
    circleRadius: Float,
    sheep: Sheep,
    showGuidelines: Boolean = false
) {

    val fluffPoints: List<Offset> = getFluffPoints(
        sheep = sheep,
        radius = circleRadius,
        circleCenter = circleCenterOffset
    )

    val fluffPath = getFluffPath(
        circleCenterOffset = circleCenterOffset,
        circleRadius = circleRadius,
        fluffPoints = fluffPoints
    )

    drawPath(path = fluffPath, color = sheep.fluffColor)

    if (showGuidelines) {
        drawFluffGuidelines(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            fluffPoints = fluffPoints
        )
    }
}

fun getFluffPath(
    circleCenterOffset: Offset,
    circleRadius: Float,
    sheep: Sheep
) = getFluffPath(
    circleCenterOffset = circleCenterOffset,
    circleRadius = circleRadius,
    fluffPoints = getFluffPoints(
        sheep = sheep,
        radius = circleRadius,
        circleCenter = circleCenterOffset
    )
)

fun getFluffPath(
    circleCenterOffset: Offset,
    circleRadius: Float,
    fluffPoints: List<Offset> = emptyList(),
) = Path().apply {
    var currentPoint = getCircumferencePointForAngle(
        Math.toRadians(0.0),
        circleRadius,
        circleCenterOffset
    )

    moveTo(currentPoint.x, currentPoint.y)

    fluffPoints.forEach { fluffPoint ->
        val controlPoint = getCurveControlPoint(currentPoint, fluffPoint, circleCenterOffset)
        quadraticBezierTo(controlPoint.x, controlPoint.y, fluffPoint.x, fluffPoint.y)
        currentPoint = fluffPoint
    }

    close()
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

private fun DrawScope.drawFluffGuidelines(
    circleCenterOffset: Offset,
    circleRadius: Float,
    fluffPoints: List<Offset>,
) {
    // Base Circle
    drawCircle(
        color = Color.Blue.copy(alpha = GuidelineAlpha.strong),
        center = center,
        radius = circleRadius
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
            color = Color.Cyan.copy(alpha = GuidelineAlpha.normal),
            radius = middlePoint.distanceToOffset(fluffPoint).div(2),
            center = middlePoint
        )

        // Lines between 2 fluff points
        drawLine(
            color = Color.Red.copy(alpha = GuidelineAlpha.strong),
            start = currentPointGuidelines,
            end = fluffPoint
        )

        currentPointGuidelines = fluffPoint
    }

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
        color = Color.Yellow.copy(alpha = GuidelineAlpha.normal),
        pointMode = PointMode.Points,
        cap = StrokeCap.Butt,
        strokeWidth = 8.dp.toPx()
    )

    // Draw axis at the end
    drawGrid()
    drawCenterAxis()
}
