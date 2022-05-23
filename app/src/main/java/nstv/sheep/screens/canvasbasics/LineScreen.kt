package nstv.sheep.screens.canvasbasics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import nstv.sheep.guidelines.GuidelineDashPattern
import nstv.sheep.guidelines.drawAxis
import nstv.sheep.guidelines.drawGrid
import nstv.sheep.sheep.extra.getSheepPathEffect

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
                pathEffect = getSheepPathEffect(miniFluffRadius)
            )
        }

        if (showGuidelines) {
            drawGrid()
            drawAxis()
        }
    }

    Spacer(modifier = Modifier.height(Grid.Two))

    CheckBoxLabel(
        text = "Show Simple Diagonal",
        checked = showSimpleDiagonal,
        onCheckedChange = { showSimpleDiagonal = it }
    )

    CheckBoxLabel(
        text = "Show Dash Pattern",
        checked = showDashPattern,
        onCheckedChange = { showDashPattern = it }
    )

    CheckBoxLabel(
        text = "Show Sheep Line",
        checked = showSheepLine,
        onCheckedChange = { showSheepLine = it }
    )

    CheckBoxLabel(
        text = "Show Guidelines",
        checked = showGuidelines,
        onCheckedChange = { showGuidelines = it }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ComposableSheepAnimationsTheme {
        LineScreen()
    }
}
