package nstv.sheepcanvas.screens.sheepscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.Color
import nstv.canvasExtensions.nextIndexLoop
import nstv.design.theme.Grid
import nstv.design.theme.SheepColor
import nstv.design.theme.components.CheckBoxLabel
import nstv.design.theme.components.SliderLabelValue
import nstv.sheep.ComposableSheep
import nstv.sheep.model.FluffStyle
import nstv.sheep.model.Sheep
import nstv.sheep.model.fourLegs
import nstv.sheep.model.twoLegsStraight
import kotlin.math.floor

private val fluffStyles = listOf(
    "Random" to FluffStyle.Random(),
    "Uniform" to FluffStyle.Uniform(10),
    "Uniform Intervals" to FluffStyle.UniformIntervals(listOf(5.0, 15.0)),
    "Circle" to FluffStyle.Uniform(10000)
)

private val legs = listOf(
    "Two Legs" to twoLegsStraight(),
    "Four Legs" to fourLegs(),
)

// Fluff to Glasses colors
private val colors = listOf(
    SheepColor.Gray to SheepColor.Black,
    SheepColor.Blue to SheepColor.Black,
    SheepColor.Green to SheepColor.Black,
    SheepColor.Purple to SheepColor.Black,
    SheepColor.Magenta to SheepColor.Black,
    SheepColor.Black to SheepColor.Gray,
    SheepColor.Orange to SheepColor.Blue,
)

@Composable
fun SheepViewerScreen(modifier: Modifier = Modifier) {
    var showGuidelines by remember { mutableStateOf(false) }
    var fluffStyleIndex by remember { mutableStateOf(0) }
    var fluffColorIndex by remember { mutableStateOf(0) }
    var legsIndex by remember { mutableStateOf(0) }
    var showBinarySheep by remember { mutableStateOf(false) }
    var showInvertBinarySheep by remember { mutableStateOf(false) }
    var sheep by remember {
        mutableStateOf(
            Sheep(
                fluffStyle = fluffStyles[fluffStyleIndex].second,
                legs = legs[legsIndex].second
            )
        )
    }

    val binarySheep = Sheep(
        fluffColor = Color.Black,
        headColor = Color.White,
        legColor = Color.White,
        glassesColor = Color.White,
    )

    val invertedBinarySheep = Sheep(
        fluffColor = Color.White,
        headColor = Color.Black,
        legColor = Color.Black,
        glassesColor = Color.Black,
    )

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(color = if (showBinarySheep) Color.Gray else Color.Unspecified),
    ) {
        val binarySheepToUse = if (showInvertBinarySheep) invertedBinarySheep else binarySheep
        ComposableSheep(
            sheep = if (showBinarySheep) binarySheepToUse else sheep,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            showGuidelines = showGuidelines
        )
        Text(
            text = "${fluffStyles[fluffStyleIndex].first} " +
                    "| ${legs[legsIndex].first} " +
                    "| ${floor(sheep.headAngle)}°"
        )

        Spacer(modifier = Modifier.height(Grid.Two))

        SliderLabelValue(
            modifier = Modifier.fillMaxWidth(),
            text = "Head Angle:",
            value = sheep.headAngle,
            onValueChange = {
                sheep = sheep.copy(headAngle = it)
            },
            valueRange = -30f..30f
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                fluffStyleIndex = fluffStyles.nextIndexLoop(fluffStyleIndex)
                sheep = sheep.copy(
                    fluffStyle = fluffStyles[fluffStyleIndex].second,
                )
            }
        ) {
            val text = "Change Fluff Style"
            Text(text = text)
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            fluffColorIndex = colors.nextIndexLoop(fluffColorIndex)
            sheep = sheep.copy(
                fluffColor = colors[fluffColorIndex].first,
                glassesColor = colors[fluffColorIndex].second,
            )
        }) {
            val text = "Change Colors"
            Text(text = text)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                legsIndex = legs.nextIndexLoop(legsIndex)
                sheep = sheep.copy(legs = legs[legsIndex].second)
            }
        ) {
            val text = "Change Legs"
            Text(text = text)
        }

        CheckBoxLabel(
            text = "Show Guidelines",
            checked = showGuidelines,
            onCheckedChange = { showGuidelines = it }
        )

        CheckBoxLabel(
            text = "Binary Sheep",
            checked = showBinarySheep,
            onCheckedChange = {
                showBinarySheep = it
            }
        )
        AnimatedVisibility(visible = showBinarySheep) {
            CheckBoxLabel(
                text = "Invert Binary Sheep",
                checked = showInvertBinarySheep,
                onCheckedChange = {
                    showInvertBinarySheep = it
                }
            )
        }
    }
}
