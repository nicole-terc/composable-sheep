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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.tooling.preview.Preview
import nstv.design.theme.ComposableSheepAnimationsTheme
import nstv.design.theme.Grid
import nstv.sheep.guidelines.GuidelineDashPattern
import nstv.sheep.guidelines.drawCenterAxis
import nstv.sheep.guidelines.drawGrid
import nstv.sheep.sheep.model.FluffStyle
import nstv.sheep.sheep.model.Sheep
import nstv.sheep.sheep.parts.getFluffPath

@Composable
fun LineScreen(modifier: Modifier = Modifier) {
    var showSimpleDiagonal by remember { mutableStateOf(false) }
    var showDashPattern by remember { mutableStateOf(false) }
    var showSheepLine by remember { mutableStateOf(false) }
    var showGuidelines by remember { mutableStateOf(true) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {

        if (showSimpleDiagonal) {
            drawLine(
                color = Color.Magenta,
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height)
            )
        }

        if (showDashPattern) {
            drawLine(
                color = Color.Cyan,
                start = size.center,
                end = Offset(0f, size.height),
                pathEffect = GuidelineDashPattern,
                strokeWidth = 10f
            )
        }

        if (showSheepLine) {
            val miniFluffRadius = size.width.div(20)
            drawLine(
                color = Color.Blue,
                start = Offset(0f, size.center.y),
                end = Offset(size.width, 0f),
                pathEffect = PathEffect.stampedPathEffect(
                    shape = getFluffPath(
                        circleCenterOffset = Offset.Zero,
                        circleRadius = miniFluffRadius,
                        sheep = Sheep(fluffStyle = FluffStyle.Uniform(10))
                    ),
                    advance = miniFluffRadius * 3f,
                    phase = miniFluffRadius,
                    style = StampedPathEffectStyle.Morph
                )
            )
        }

        if (showGuidelines) {
            drawGrid()
            drawCenterAxis()
        }
    }

    Spacer(modifier = Modifier.height(Grid.Two))

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showSimpleDiagonal = !showSimpleDiagonal }
    ) {
        val text =
            if (showSimpleDiagonal) "Hide Simple Diagonal" else "Show Simple Diagonal"
        Text(text = text)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showDashPattern = !showDashPattern }
    ) {
        val text =
            if (showDashPattern) "Hide Dash Pattern" else "Show Dash Pattern"
        Text(text = text)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showSheepLine = !showSheepLine }
    ) {
        val text =
            if (showSheepLine) "Hide Sheep Line" else "Show Sheep Line"
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
private fun PreviewSheepTwoLeg() {
    ComposableSheepAnimationsTheme {
        LineScreen()
    }
}
