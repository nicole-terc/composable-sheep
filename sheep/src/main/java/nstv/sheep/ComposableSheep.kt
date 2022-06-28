package nstv.sheep

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
fun ComposableSheep(
    modifier: Modifier,
    sheep: Sheep,
    fluffColor: Color = sheep.fluffColor,
    headColor: Color = sheep.headColor,
    legColor: Color = sheep.legColor,
    eyeColor: Color = sheep.eyeColor,
    glassesColor: Color = sheep.glassesColor,
    glassesTranslation: Float = sheep.glassesTranslation,
    showGuidelines: Boolean = false,
) {
    ComposableSheep(
        modifier = modifier,
        sheep = sheep,
        fluffBrush = SolidColor(fluffColor),
        headColor = headColor,
        legColor = legColor,
        eyeColor = eyeColor,
        glassesColor = glassesColor,
        glassesTranslation = glassesTranslation,
        showGuidelines = showGuidelines
    )
}

@Composable
fun ComposableSheep(
    modifier: Modifier,
    sheep: Sheep,
    fluffBrush: Brush,
    headColor: Color = sheep.headColor,
    legColor: Color = sheep.legColor,
    eyeColor: Color = sheep.eyeColor,
    glassesColor: Color = sheep.glassesColor,
    glassesTranslation: Float = sheep.glassesTranslation,
    showGuidelines: Boolean = false,
) {
    Canvas(modifier = modifier) {
        val circleRadius = size.width * 0.3f
        val circleCenterOffset = Offset(size.width / 2f, size.height / 2f)

        drawLegs(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            sheep = sheep,
            legColor = legColor,
            showGuidelines = showGuidelines
        )

        drawFluff(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            sheep = sheep,
            fluffBrush = fluffBrush,
            showGuidelines = showGuidelines
        )

        drawHead(
            circleCenterOffset = circleCenterOffset,
            circleRadius = circleRadius,
            sheep = sheep,
            headColor = headColor,
            eyeColor = eyeColor,
            glassesColor = glassesColor,
            glassesTranslation = glassesTranslation,
            showGuidelines = showGuidelines
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheep() {
    ComposableSheepTheme {
        ComposableSheep(modifier = Modifier.size(300.dp), sheep = Sheep(FluffStyle.Random()))
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheepTwoLeg() {
    ComposableSheepTheme {
        ComposableSheep(
            modifier = Modifier.size(300.dp),
            sheep = Sheep(FluffStyle.Random(), legs = twoLegsStraight())
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun PreviewSheepGuidelines() {
    ComposableSheepTheme {
        ComposableSheep(
            modifier = Modifier.size(300.dp),
            sheep = Sheep(FluffStyle.Random()),
            showGuidelines = true
        )
    }
}
