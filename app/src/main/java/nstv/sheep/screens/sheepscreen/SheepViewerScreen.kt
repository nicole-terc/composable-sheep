package nstv.sheep.screens.sheepscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nstv.sheep.sheep.Sheep
import nstv.sheep.sheep.SheepComposable
import nstv.sheep.sheep.fluff.FluffStyle

val fluffStyles = listOf(
    "Random" to FluffStyle.Random(),
    "Uniform" to FluffStyle.Uniform(10),
    "Uniform Intervals" to FluffStyle.UniformIntervals(listOf(5.0, 15.0))
)

@Composable
fun SheepViewerScreen() {
    SheepViewer()
}

@Composable
private fun SheepViewer() {
    val showGuidelines = remember { mutableStateOf(false) }
    val fluffStyleIndex = remember { mutableStateOf(0) }
    val sheep =
        remember { mutableStateOf(Sheep(fluffStyles[fluffStyleIndex.value].second)) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        SheepComposable(
            sheep = sheep.value,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            showGuidelines = showGuidelines.value
        )
        Text(text = fluffStyles[fluffStyleIndex.value].first)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                fluffStyleIndex.value =
                    if (fluffStyleIndex.value + 1 >= fluffStyles.size) {
                        0
                    } else {
                        fluffStyleIndex.value + 1
                    }
                sheep.value = Sheep(fluffStyles[fluffStyleIndex.value].second)
            }
        ) {
            val text = "Change Fluff Style"
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
