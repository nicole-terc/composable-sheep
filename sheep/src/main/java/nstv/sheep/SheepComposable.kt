package nstv.sheep

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.design.theme.ComposableSheepTheme
import nstv.sheep.model.FluffStyle
import nstv.sheep.model.Sheep
import nstv.sheep.model.twoLegsStraight
import nstv.sheep.parts.drawFluff
import nstv.sheep.parts.drawHead
import nstv.sheep.parts.drawLegs

@Composable
fun SheepComposable(
    modifier: Modifier,
    sheep: Sheep,
    showGuidelines: Boolean = false,
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val circleRadius = canvasWidth * 0.3f
        val circleCenterOffset = Offset(canvasWidth / 2f, canvasHeight / 2f)

        drawLegs(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            sheep = sheep,
            showGuidelines = showGuidelines
        )

        drawFluff(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            sheep = sheep,
            showGuidelines = showGuidelines
        )

        drawHead(
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
    ComposableSheepTheme {
        SheepComposable(modifier = Modifier.size(300.dp), sheep = Sheep(FluffStyle.Random()))
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheepTwoLeg() {
    ComposableSheepTheme {
        SheepComposable(
            modifier = Modifier.size(300.dp),
            sheep = Sheep(FluffStyle.Random(), legs = twoLegsStraight())
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheepGuidelines() {
    ComposableSheepTheme {
        SheepComposable(
            modifier = Modifier.size(300.dp),
            sheep = Sheep(FluffStyle.Random()),
            showGuidelines = true
        )
    }
}
