package nstv.sheep.parts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import nstv.canvasExtensions.guidelines.drawPoint
import nstv.design.theme.SheepColor

fun DrawScope.drawEyes(
    headTopLeft: Offset,
    headSize: Size,
    headAngle: Float,
    headCenter: Offset,
    color: Color = SheepColor.Eye,
    singleEyeHeadRatio: Float = 0.15f,
    eyeOverlapPercentage: Float = 0.4f,
    eyeOffsetFromHeadCenter: Float = 1.5f,
    eyeSeparationPercentage: Float = 1.2f,
    showGuidelines: Boolean = false
) {
    val eyeDiameter = headSize.width.times(singleEyeHeadRatio)
    val eyeRadius = eyeDiameter.div(2f)

    val pupilSize = Size(
        width = eyeDiameter.times(0.75f),
        height = eyeDiameter.times(0.2f)
    )

    val leftEyeCenter = Offset(
        x = headCenter.x - eyeRadius.times(eyeOffsetFromHeadCenter),
        y = headTopLeft.y - eyeDiameter.times(1f - eyeOverlapPercentage) + eyeRadius
    )

    val rightEyeCenter = Offset(
        x = leftEyeCenter.x + eyeDiameter.times(1f + eyeSeparationPercentage),
        y = leftEyeCenter.y
    )

    val eyeAngle = headAngle + 10f

    rotate(
        degrees = eyeAngle,
        pivot = headCenter
    ) {

        listOf(leftEyeCenter, rightEyeCenter).forEach { eyeCenter ->
            drawCircle(
                color = color,
                radius = eyeRadius,
                center = eyeCenter
            )

            drawRoundRect(
                color = Color.Black,
                topLeft = eyeCenter - pupilSize.center,
                size = pupilSize
            )
        }
    }

    if (showGuidelines) {
        drawEyeGuidelines(
            center = leftEyeCenter,
            degrees = eyeAngle,
            headCenter = headCenter
        )

        drawEyeGuidelines(
            center = rightEyeCenter,
            degrees = eyeAngle,
            headCenter = headCenter
        )
    }
}

private fun DrawScope.drawEyeGuidelines(
    center: Offset,
    degrees: Float,
    headCenter: Offset,
) {
    rotate(
        degrees = degrees,
        pivot = headCenter
    ) {
        drawPoint(
            offset = center,
        )
    }
}
