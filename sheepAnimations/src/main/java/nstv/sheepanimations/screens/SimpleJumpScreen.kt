package nstv.sheepanimations.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.dp
import nstv.design.theme.TextUnit
import nstv.design.theme.components.SliderLabelValue
import nstv.sheep.SheepComposable
import nstv.sheepanimations.model.SheepJumpingOffset
import nstv.sheepanimations.model.SheepUiState

@Composable
fun SimpleJumpScreen(
    modifier: Modifier = Modifier,
) {
    var sheepUiState by remember { mutableStateOf(SheepUiState()) }

    // This declarations can be contained in the UiState, added here for clarity
    var offsetY by remember { mutableStateOf(0.dp) }
    var dampingRatio by remember { mutableStateOf(Spring.DampingRatioNoBouncy) }
    var stiffness by remember { mutableStateOf(Spring.StiffnessMedium) }

    LaunchedEffect(sheepUiState.isJumping) {
        if (sheepUiState.isJumping) {
            // Jump up
            animate(
                0f, SheepJumpingOffset,
                animationSpec = spring(
                    dampingRatio = dampingRatio,
                    stiffness = stiffness,
                )
            ) { value, _ ->
                offsetY = value.dp
            }

            // Jump down
            animate(
                SheepJumpingOffset, 0f,
                animationSpec = spring(
                    dampingRatio = dampingRatio,
                    stiffness = stiffness,
                )
            ) { value, _ ->
                offsetY = value.dp
            }

            // update jumping state when done
            sheepUiState = sheepUiState.copy(isJumping = false)
        } else {
            animate(offsetY.value, 0f) { value, _ ->
                offsetY = value.dp
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {

        SheepComposable(
            sheep = sheepUiState.sheep,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .align(Alignment.CenterHorizontally)
                .offset(y = offsetY)
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            sheepUiState = sheepUiState.copy(
                isJumping = !sheepUiState.isJumping
            )
        }) {
            Text(text = "Sheep it!", fontWeight = FontWeight.Bold, fontSize = TextUnit.Twenty)
        }

        SliderLabelValue(
            modifier = Modifier.fillMaxWidth(),
            text = "Damping Ratio",
            value = dampingRatio.times(100),
            onValueChange = { dampingRatio = it.div(100) },
            valueRange = 20f..200f
        )

        SliderLabelValue(
            modifier = Modifier.fillMaxWidth(),
            text = "Stiffness",
            value = stiffness,
            onValueChange = { stiffness = it },
            valueRange = Spring.StiffnessVeryLow..Spring.StiffnessHigh,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    SimpleJumpScreen()
}
