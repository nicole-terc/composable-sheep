package nstv.sheep.parts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import nstv.canvasExtensions.FullCircleAngleInRadians
import nstv.canvasExtensions.distanceToOffset
import nstv.canvasExtensions.getCircumferencePointForAngle
import nstv.canvasExtensions.getCurveControlPoint
import nstv.canvasExtensions.getMiddlePoint
import nstv.canvasExtensions.guidelines.GuidelineAlpha
import nstv.canvasExtensions.guidelines.drawAxis
import nstv.canvasExtensions.guidelines.drawGrid
import nstv.sheep.model.FluffStyle
import nstv.sheep.model.Sheep

fun DrawScope.drawFluff(
    sheep: Sheep,
    circleRadius: Float,
    circleCenterOffset: Offset,
    showGuidelines: Boolean = false
) {

    val fluffPoints: List<Offset> = getFluffPoints(
        fluffPercentages = sheep.fluffStyle.fluffChunksPercentages,
        radius = circleRadius,
        circleCenter = circleCenterOffset
    )

    val fluffPath = getFluffPath(
        fluffPoints = fluffPoints,
        circleRadius = circleRadius,
        circleCenterOffset = circleCenterOffset,
    )

    drawPath(path = fluffPath, color = sheep.fluffColor)

    if (showGuidelines) {
        drawFluffGuidelines(
            fluffPoints = fluffPoints,
            circleRadius = circleRadius,
            circleCenterOffset = circleCenterOffset,
        )
    }
}

/**
 * Returns the coordinates (points) of the middle points between fluff chunks.
 */
private fun getFluffPoints(
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

fun getFluffPath(
    circleRadius: Float,
    circleCenterOffset: Offset,
    fluffStyle: FluffStyle = FluffStyle.Random(),
) = getFluffPath(
    fluffPoints = getFluffPoints(
        fluffPercentages = fluffStyle.fluffChunksPercentages,
        radius = circleRadius,
        circleCenter = circleCenterOffset
    ),
    circleRadius = circleRadius,
    circleCenterOffset = circleCenterOffset,
)

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

private fun DrawScope.drawFluffGuidelines(
    fluffPoints: List<Offset>,
    circleRadius: Float,
    circleCenterOffset: Offset,
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
    drawAxis()
}
