package nstv.sheepanimations.screens

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nstv.canvasExtensions.nextIndexLoop
import nstv.design.theme.SheepColor
import nstv.design.theme.TextUnit
import nstv.design.theme.components.CheckBoxLabel
import nstv.design.theme.components.SliderLabelValue
import nstv.design.theme.components.StartStopBehaviorButton
import nstv.sheep.SheepComposable
import nstv.sheepanimations.model.SheepJumpSize
import nstv.sheepanimations.model.SheepJumpingOffset
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
fun AllInChaosScreen(
    modifier: Modifier = Modifier,
) {
    val verticalScroll = rememberScrollState()
    var sheepUiState by remember { mutableStateOf(SheepUiState()) }

    // This declarations can be contained in the UiState, added here for clarity

    // JUMP
    var offsetY by remember { mutableStateOf(0.dp) }

    var dampingRatio by remember { mutableStateOf(Spring.DampingRatioNoBouncy) }
    var stiffness by remember { mutableStateOf(Spring.StiffnessMedium) }

    // COLOR
    var colorIndex by remember { mutableStateOf(0) }
    val color = remember { Animatable(colors[0]) }

    // VISIBILITY
    var alpha by remember { mutableStateOf(1f) }

    LaunchedEffect(sheepUiState) {
        verticalScroll.animateScrollTo(if (sheepUiState.isAnimating) 0 else verticalScroll.maxValue)

        if (sheepUiState.animationsEnabled) {
            launch {
                while (sheepUiState.isGroovy) {
                    colorIndex = colors.nextIndexLoop(colorIndex)
                    color.animateTo(
                        colors[colorIndex],
                        animationSpec = tween(durationMillis = 500, delayMillis = 200)
                    )
                }
            }
            launch {
                while (sheepUiState.isJumping) {
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
                }
            }
            launch {
                while (sheepUiState.isBlinking) {
                    animate(
                        0.2f,
                        1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 200, delayMillis = 200),
                            repeatMode = RepeatMode.Reverse
                        )
                    ) { value, _ ->
                        alpha = value
                    }
                }
            }
        } else {
            // Initial state
            animate(offsetY.value, 0f) { value, _ ->
                offsetY = value.dp
            }
            colorIndex = 0
            color.snapTo(colors[colorIndex])
            alpha = 1f
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .animateContentSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Spacer(modifier = Modifier.height(SheepJumpSize))
        SheepComposable(
            sheep = sheepUiState.sheep,
            fluffColor = color.value,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .align(Alignment.CenterHorizontally)
                .offset(y = offsetY)
                .alpha(alpha)
        )

        StartStopBehaviorButton(
            isBehaviorActive = sheepUiState.animationsEnabled,
            enabled = sheepUiState.hasAnimations || sheepUiState.animationsEnabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            onClick = {
                sheepUiState = sheepUiState.copy(
                    animationsEnabled = !sheepUiState.animationsEnabled
                )
            }
        ) {
            val text = if (sheepUiState.animationsEnabled) "Shtop it!" else "Sheep it!"
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = TextUnit.Twenty)
        }

        // BLINKING
        CheckBoxLabel(text = "Blinking", checked = sheepUiState.isBlinking, onCheckedChange = {
            sheepUiState = sheepUiState.copy(isBlinking = !sheepUiState.isBlinking)
        })

        // COLORS
        CheckBoxLabel(text = "Groovy", checked = sheepUiState.isGroovy, onCheckedChange = {
            sheepUiState = sheepUiState.copy(isGroovy = !sheepUiState.isGroovy)
        })

        // JUMP
        CheckBoxLabel(text = "Jumping", checked = sheepUiState.isJumping, onCheckedChange = {
            sheepUiState = sheepUiState.copy(isJumping = !sheepUiState.isJumping)
        })

        if (sheepUiState.isJumping) {
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
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    AllInChaosScreen()
}
