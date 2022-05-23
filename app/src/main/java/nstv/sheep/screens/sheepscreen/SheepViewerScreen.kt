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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nstv.design.theme.Grid
import nstv.design.theme.components.CheckBoxLabel
import nstv.sheep.extensions.nextIndexLoop
import nstv.sheep.sheep.SheepComposable
import nstv.sheep.sheep.model.FluffStyle
import nstv.sheep.sheep.model.FourLegs
import nstv.sheep.sheep.model.Sheep
import nstv.sheep.sheep.model.TwoLegsStraight
import kotlin.math.floor

private val fluffStyles = listOf(
    "Random" to FluffStyle.Random(),
    "Uniform" to FluffStyle.Uniform(10),
    "Uniform Intervals" to FluffStyle.UniformIntervals(listOf(5.0, 15.0)),
    "Circle" to FluffStyle.Uniform(10000)
)

private val legs = listOf(
    "Two Legs" to TwoLegsStraight(),
    "Four Legs" to FourLegs(),
)

@Composable
fun SheepViewerScreen(modifier: Modifier = Modifier) {
    var showGuidelines by remember { mutableStateOf(false) }
    var fluffStyleIndex by remember { mutableStateOf(0) }
    var legsIndex by remember { mutableStateOf(0) }
    var sheep by
    remember {
        mutableStateOf(
            Sheep(
                fluffStyle = fluffStyles[fluffStyleIndex].second,
                legs = legs[legsIndex].second
            )
        )
    }

    Column(
        modifier = modifier
    ) {
        SheepComposable(
            sheep = sheep,
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

        val valueRange = -30f..30f

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Head Angle:")
            Slider(
                value = sheep.headAngle,
                valueRange = valueRange,
                onValueChange = {
                    sheep = sheep.copy(headAngle = it)
                }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                fluffStyleIndex = fluffStyles.nextIndexLoop(fluffStyleIndex)
                sheep = Sheep(
                    fluffStyle = fluffStyles[fluffStyleIndex].second,
                    legs = legs[legsIndex].second,
                    headAngle = sheep.headAngle
                )
            }
        ) {
            val text = "Change Fluff Style"
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
    }
}
