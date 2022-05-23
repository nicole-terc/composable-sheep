package nstv.sheep.screens.canvasbasics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import nstv.design.theme.ComposableSheepAnimationsTheme
import nstv.design.theme.Grid
import nstv.sheep.guidelines.GuidelineAlpha
import nstv.sheep.guidelines.drawCenterPoint
import nstv.sheep.guidelines.drawGrid
import nstv.sheep.guidelines.drawRectGuideline

@Composable
fun ShapeScreen(modifier: Modifier = Modifier) {
    var showCircle by remember { mutableStateOf(false) }
    var showRect by remember { mutableStateOf(false) }
    var showRoundRect by remember { mutableStateOf(false) }
    var showOval by remember { mutableStateOf(false) }
    var showGuidelines by remember { mutableStateOf(true) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {

        val shapeSize = Size(size.width.times(0.9f), size.height.times(0.6f))

        val topLeft = Offset(
            x = size.center.x - shapeSize.width.div(2f),
            y = size.center.y - shapeSize.height.div(2f),
        )

        if (showCircle) {
            drawCircle(
                color = Color.Blue.copy(alpha = GuidelineAlpha.normal),
                radius = shapeSize.minDimension.div(2),
                center = size.center
            )
        }

        if (showRect) {
            drawRect(
                color = Color.Red.copy(alpha = GuidelineAlpha.normal),
                size = shapeSize,
                topLeft = topLeft
            )
        }

        if (showRoundRect) {
            drawRoundRect(
                color = Color.Cyan.copy(alpha = GuidelineAlpha.normal),
                size = shapeSize,
                topLeft = topLeft,
                cornerRadius = CornerRadius(100f)
            )
        }

        if (showOval) {
            drawOval(
                color = Color.Yellow.copy(alpha = GuidelineAlpha.normal),
                size = shapeSize,
                topLeft = topLeft
            )
        }

        if (showGuidelines) {
            drawGrid()
            drawCenterPoint()

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

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showCircle = !showCircle }
    ) {
        val text =
            (if (showCircle) "Hide " else "Show ") + "Circle"
        Text(text = text)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showRect = !showRect }
    ) {
        val text =
            (if (showRect) "Hide " else "Show ") + "Rect"
        Text(text = text)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showRoundRect = !showRoundRect }
    ) {
        val text =
            (if (showRoundRect) "Hide " else "Show ") + "Round Rect"
        Text(text = text)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showOval = !showOval }
    ) {
        val text =
            (if (showOval) "Hide " else "Show ") + "Oval"
        Text(text = text)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showGuidelines = !showGuidelines }
    ) {
        val text =
            if (showGuidelines) "Hide Guidelines" else "Show Guidelines"
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ComposableSheepAnimationsTheme {
        LineScreen()
    }
}
