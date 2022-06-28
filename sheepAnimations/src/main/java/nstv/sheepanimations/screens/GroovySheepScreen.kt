package nstv.sheepanimations.screens

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import nstv.canvasExtensions.nextIndexLoop
import nstv.design.theme.SheepColor
import nstv.design.theme.TextUnit
import nstv.sheep.ComposableSheep
import nstv.sheepanimations.model.SheepUiState

private val colors = listOf(
    SheepColor.Gray,
    SheepColor.Blue,
    SheepColor.Green,
    SheepColor.Purple,
    SheepColor.Magenta,
    SheepColor.Orange,
)

@Composable
fun GroovySheepScreen(
    modifier: Modifier = Modifier,
) {
    var sheepUiState by remember { mutableStateOf(SheepUiState()) }

    // This declaration can be contained in the UiState, added here for clarity
    var colorIndex by remember { mutableStateOf(0) }

    val color = remember { Animatable(colors[0]) }

    LaunchedEffect(colorIndex) {
        if (sheepUiState.isGroovy) {
            color.animateTo(
                colors[colorIndex],
                animationSpec = tween(durationMillis = 500, delayMillis = 200)
            )
            colorIndex = colors.nextIndexLoop(colorIndex)
        } else {
            colorIndex = 0
            color.snapTo(colors[colorIndex])
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {

        ComposableSheep(
            sheep = sheepUiState.sheep,
            fluffColor = color.value,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .align(Alignment.CenterHorizontally)
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            colorIndex = colors.nextIndexLoop(colorIndex)
            sheepUiState = sheepUiState.copy(isGroovy = !sheepUiState.isGroovy)
        }) {
            Text(text = "Sheep it!", fontWeight = FontWeight.Bold, fontSize = TextUnit.Twenty)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    GroovySheepScreen()
}
