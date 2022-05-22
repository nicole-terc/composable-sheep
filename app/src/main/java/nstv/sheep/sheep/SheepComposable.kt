package nstv.sheep.sheep

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.design.theme.ComposableSheepAnimationsTheme
import nstv.sheep.sheep.fluff.FluffStyle
import nstv.sheep.sheep.fluff.drawFluff

@Composable
fun SheepComposable(
    sheep: Sheep,
    modifier: Modifier = Modifier,
    showGuidelines: Boolean = false,
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val circleRadius = size.width * 0.3f
        val circleCenterOffset = Offset(canvasWidth / 2f, canvasHeight / 2f)

        drawFluff(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            sheep = sheep,
            showGuidelines = showGuidelines
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheep() {
    ComposableSheepAnimationsTheme {
        SheepComposable(modifier = Modifier.size(300.dp), sheep = Sheep(FluffStyle.Random()))
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheepGuidelines() {
    ComposableSheepAnimationsTheme {
        SheepComposable(
            modifier = Modifier.size(300.dp),
            sheep = Sheep(FluffStyle.Random()),
            showGuidelines = true
        )
    }
}
