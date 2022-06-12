package nstv.sheep.screens.canvasbasics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nstv.design.theme.ComposableSheepAnimationsTheme
import nstv.design.theme.Grid
import nstv.design.theme.components.CheckBoxLabel
import nstv.design.theme.components.LabeledText
import nstv.design.theme.components.SliderLabelValue
import nstv.sheep.extensions.nextIndexLoop
import nstv.sheep.guidelines.GuidelineAlpha
import nstv.sheep.guidelines.drawAxis
import nstv.sheep.guidelines.drawGrid
import nstv.sheep.guidelines.drawPoint
import nstv.sheep.maths.getCircumferencePointForAngle

@Composable
fun ArcScreen(
    modifier: Modifier = Modifier
) {
    var startAngle by remember { mutableStateOf(0f) }
    var sweepAngleBack by remember { mutableStateOf(90f) }
    var sweepAngleFront by remember { mutableStateOf(90f) }
    var drawStyleIndex by remember { mutableStateOf(1) }
    var useCenter by remember { mutableStateOf(false) }
    var showSmallArc by remember { mutableStateOf(false) }

    var showGuidelines by remember { mutableStateOf(true) }

    val backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer
    val foregroundColor = MaterialTheme.colorScheme.primaryContainer

    val backgroundColorSmall = MaterialTheme.colorScheme.onTertiaryContainer
    val foregroundColorSmall = MaterialTheme.colorScheme.tertiaryContainer

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            val drawStyle = drawStyleOptions[drawStyleIndex].second

            drawArc(
                color = backgroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngleBack,
                useCenter = useCenter,
                topLeft = Offset(0f, 0f),
                alpha = GuidelineAlpha.strong,
                size = size,
                style = drawStyle
            )

            drawArc(
                color = foregroundColor,
                startAngle = startAngle,
                sweepAngle = sweepAngleFront,
                useCenter = useCenter,
                topLeft = Offset(0f, 0f),
                size = size,
                alpha = GuidelineAlpha.strong,
                style = drawStyle
            )

            if (showSmallArc) {
                val smallArcTopLeft = Offset(size.width.times(0.2f), size.height.times(0.2f))
                val smallArcSize = size.div(4f)
                drawArc(
                    color = backgroundColorSmall,
                    startAngle = startAngle,
                    sweepAngle = sweepAngleBack,
                    useCenter = useCenter,
                    topLeft = smallArcTopLeft,
                    size = smallArcSize,
                    alpha = GuidelineAlpha.strong,
                    style = drawStyle
                )

                drawArc(
                    color = foregroundColorSmall,
                    startAngle = startAngle,
                    sweepAngle = sweepAngleFront,
                    useCenter = useCenter,
                    topLeft = smallArcTopLeft,
                    size = smallArcSize,
                    alpha = GuidelineAlpha.strong,
                    style = drawStyle
                )
                if (showGuidelines) {
                    val smallCircleCenter = smallArcTopLeft + smallArcSize.center
                    drawPoint(offset = smallCircleCenter)
                    drawLine(
                        color = Color.Red,
                        start = smallCircleCenter,
                        end = getCircumferencePointForAngle(
                            angleInRadians = Math.toRadians(startAngle.toDouble()),
                            radius = smallArcSize.width.div(2),
                            circleCenter = smallCircleCenter
                        ),
                        strokeWidth = 5f
                    )
                }
            }

            if (showGuidelines) {
                drawGrid()
                drawAxis(color = Color.Red)
                drawLine(
                    color = Color.Green,
                    start = size.center,
                    end = getCircumferencePointForAngle(
                        angleInRadians = Math.toRadians(startAngle.toDouble()),
                        radius = size.width.div(2),
                        circleCenter = size.center
                    ),
                    strokeWidth = 5f
                )
            }
        }

        Spacer(modifier = Modifier.height(Grid.Two))

        LabeledText("DrawStyle: ", drawStyleOptions[drawStyleIndex].first)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                drawStyleIndex = drawStyleOptions.nextIndexLoop(drawStyleIndex)
            }
        ) {
            val text = "Change DrawStyle"
            Text(text = text)
        }

        val angleRange = 0f..360f

        SliderLabelValue(
            modifier = Modifier.fillMaxWidth(),
            text = "Start Angle:",
            value = startAngle,
            onValueChange = { startAngle = it },
            valueRange = angleRange
        )

        SliderLabelValue(
            modifier = Modifier.fillMaxWidth(),
            text = "Sweep Angle:",
            value = sweepAngleBack,
            onValueChange = { sweepAngleBack = it },
            valueRange = angleRange,
            onValueChangeFinished = { sweepAngleFront = sweepAngleBack }
        )

        CheckBoxLabel(
            text = "Use Center",
            checked = useCenter,
            onCheckedChange = { useCenter = it }
        )

        CheckBoxLabel(
            text = "Show Small Arc",
            checked = showSmallArc,
            onCheckedChange = { showSmallArc = it }
        )

        CheckBoxLabel(
            text = "Show Guidelines",
            checked = showGuidelines,
            onCheckedChange = { showGuidelines = it }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ComposableSheepAnimationsTheme {
        ArcScreen()
    }
}
