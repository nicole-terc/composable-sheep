package nstv.sheepcanvas.screens.canvasbasics

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nstv.canvasExtensions.guidelines.GuidelineAlpha
import nstv.canvasExtensions.guidelines.drawGrid
import nstv.canvasExtensions.guidelines.drawPoint
import nstv.canvasExtensions.guidelines.drawRectGuideline
import nstv.canvasExtensions.nextIndexLoop
import nstv.design.theme.ComposableSheepTheme
import nstv.design.theme.Grid
import nstv.design.theme.components.CheckBoxLabel
import nstv.design.theme.components.LabeledText

@Composable
fun ShapeScreen(modifier: Modifier = Modifier) {
    var showCircle by remember { mutableStateOf(false) }
    var showRect by remember { mutableStateOf(false) }
    var showRoundRect by remember { mutableStateOf(false) }
    var showOval by remember { mutableStateOf(false) }
    var showGuidelines by remember { mutableStateOf(true) }
    var drawStyleIndex by remember { mutableStateOf(0) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            val drawStyle = drawStyleOptions[drawStyleIndex].second

            val shapeSize = Size(size.width.times(0.9f), size.height.times(0.6f))

            val topLeft = Offset(
                x = size.center.x - shapeSize.width.div(2f),
                y = size.center.y - shapeSize.height.div(2f),
            )

            if (showCircle) {
                drawCircle(
                    color = Color.Blue.copy(alpha = GuidelineAlpha.normal),
                    radius = shapeSize.minDimension.div(2),
                    center = size.center,
                    style = drawStyle
                )
            }

            if (showRect) {
                drawRect(
                    color = Color.Red.copy(alpha = GuidelineAlpha.normal),
                    size = shapeSize,
                    topLeft = topLeft,
                    style = drawStyle
                )
            }

            if (showRoundRect) {
                drawRoundRect(
                    color = Color.Cyan.copy(alpha = GuidelineAlpha.normal),
                    size = shapeSize,
                    topLeft = topLeft,
                    cornerRadius = CornerRadius(100f),
                    style = drawStyle
                )
            }

            if (showOval) {
                drawOval(
                    color = Color.Yellow.copy(alpha = GuidelineAlpha.normal),
                    size = shapeSize,
                    topLeft = topLeft,
                    style = drawStyle
                )
            }

            if (showGuidelines) {
                drawGrid()
                drawPoint()

                if (showOval || showRect || showRoundRect) {
                    drawRectGuideline(
                        size = shapeSize,
                        topLeft = topLeft,
                        color = Color.DarkGray
                    )
                }
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

        CheckBoxLabel(
            text = "Show Circle",
            checked = showCircle,
            onCheckedChange = { showCircle = it }
        )

        CheckBoxLabel(
            text = "Show Rect",
            checked = showRect,
            onCheckedChange = { showRect = it }
        )

        CheckBoxLabel(
            text = "Show Round Rect",
            checked = showRoundRect,
            onCheckedChange = { showRoundRect = it }
        )

        CheckBoxLabel(
            text = "Show Oval",
            checked = showOval,
            onCheckedChange = { showOval = it }
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
    ComposableSheepTheme {
        ShapeScreen()
    }
}
