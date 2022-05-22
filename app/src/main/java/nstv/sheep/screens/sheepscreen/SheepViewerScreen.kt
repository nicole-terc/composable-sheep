package nstv.sheep.screens.sheepscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nstv.design.theme.Grid
import nstv.sheep.sheep.SheepComposable
import nstv.sheep.sheep.model.FluffStyle
import nstv.sheep.sheep.model.FourLegs
import nstv.sheep.sheep.model.Sheep
import nstv.sheep.sheep.model.TwoLegsStraight
import kotlin.math.floor

val fluffStyles = listOf(
    "Random" to FluffStyle.Random(),
    "Uniform" to FluffStyle.Uniform(10),
    "Uniform Intervals" to FluffStyle.UniformIntervals(listOf(5.0, 15.0)),
    "Circle" to FluffStyle.Uniform(10000)
)

val legs = listOf(
    "Two Legs" to TwoLegsStraight(),
    "Four Legs" to FourLegs(),
)

@Composable
fun SheepViewerScreen(modifier: Modifier = Modifier) {
    val showGuidelines = remember { mutableStateOf(false) }
    val fluffStyleIndex = remember { mutableStateOf(0) }
    val legsIndex = remember { mutableStateOf(0) }
    val sheep =
        remember {
            mutableStateOf(
                Sheep(
                    fluffStyle = fluffStyles[fluffStyleIndex.value].second,
                    legs = legs[legsIndex.value].second
                )
            )
        }

    Column(
        modifier = modifier
    ) {
        SheepComposable(
            sheep = sheep.value,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            showGuidelines = showGuidelines.value
        )
        Text(
            text = "${fluffStyles[fluffStyleIndex.value].first} " +
                "| ${legs[legsIndex.value].first} " +
                "| ${floor(sheep.value.headAngle)}°"
        )

        Spacer(modifier = Modifier.height(Grid.Two))

        val valueRange = -30f..30f

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Head Angle:")
            Slider(
                value = sheep.value.headAngle,
                valueRange = valueRange,
                onValueChange = {
                    sheep.value = sheep.value.copy(headAngle = it)
                }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                fluffStyleIndex.value =
                    if (fluffStyleIndex.value + 1 >= fluffStyles.size) {
                        0
                    } else {
                        fluffStyleIndex.value + 1
                    }
                sheep.value = Sheep(
                    fluffStyle = fluffStyles[fluffStyleIndex.value].second,
                    legs = legs[legsIndex.value].second,
                    headAngle = sheep.value.headAngle
                )
            }
        ) {
            val text = "Change Fluff Style"
            Text(text = text)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                legsIndex.value =
                    if (legsIndex.value + 1 >= legs.size) {
                        0
                    } else {
                        legsIndex.value + 1
                    }
                sheep.value = sheep.value.copy(legs = legs[legsIndex.value].second)
            }
        ) {
            val text = "Change Legs"
            Text(text = text)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showGuidelines.value = !showGuidelines.value }
        ) {
            val text =
                if (showGuidelines.value) "Hide Guidelines" else "Show Guidelines"
            Text(text = text)
        }
    }
}
