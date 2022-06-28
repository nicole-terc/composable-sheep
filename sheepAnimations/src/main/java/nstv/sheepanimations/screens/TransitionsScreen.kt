package nstv.sheepanimations.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nstv.canvasExtensions.nextItemLoop
import nstv.design.theme.SheepColor
import nstv.design.theme.TextUnit
import nstv.design.theme.components.StartStopBehaviorButton
import nstv.sheep.SheepComposable
import nstv.sheepanimations.model.SheepCanvasSize
import nstv.sheepanimations.model.SheepJumpSize
import nstv.sheepanimations.model.SheepUiState
import nstv.sheepanimations.screens.SheepJumpState.Crouch
import nstv.sheepanimations.screens.SheepJumpState.End
import nstv.sheepanimations.screens.SheepJumpState.Start
import nstv.sheepanimations.screens.SheepJumpState.Top

enum class SheepJumpState {
    Start,
    Crouch,
    Top,
    End,
}

private class JumpTransitionData(
    offsetY: State<Dp>,
    color: State<Color>,
    headAngle: State<Float>,
    glassesTranslation: State<Float>,
    sheepScale: State<Offset>
) {
    val offsetY by offsetY
    val color by color
    val headAngle by headAngle
    val glassesTranslation by glassesTranslation
    val sheepScale by sheepScale
}

@Composable
fun TransitionsScreen(
    modifier: Modifier = Modifier,
) {
    val verticalScroll = rememberScrollState()
    var sheepUiState by remember { mutableStateOf(SheepUiState()) }
    var jumpState by remember { mutableStateOf(Start) }

    LaunchedEffect(sheepUiState) {
        launch {
            while (sheepUiState.animationsEnabled) {
                jumpState =
                    SheepJumpState.values().nextItemLoop(SheepJumpState.values().indexOf(jumpState))
                delay(jumpState.getDelayAfter())
            }
            jumpState = Start
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .animateContentSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        JumpingSheep(jumpState = jumpState, sheepUiState = sheepUiState)

        StartStopBehaviorButton(
            isBehaviorActive = sheepUiState.animationsEnabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            onClick = {
                sheepUiState =
                    sheepUiState.copy(animationsEnabled = !sheepUiState.animationsEnabled)
            }
        ) {
            val text = if (sheepUiState.animationsEnabled) "Shtop it!" else "Sheep it!"
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = TextUnit.Twenty)
        }
    }
}

@Composable
private fun JumpingSheep(
    jumpState: SheepJumpState,
    sheepUiState: SheepUiState,
    modifier: Modifier = Modifier
) {
    val jumpTransitionData = updateJumpTransitionData(jumpState)

    Box(
        modifier = modifier
            .height(SheepJumpSize + SheepCanvasSize)
            .fillMaxWidth()
    ) {
        SheepComposable(
            sheep = sheepUiState.sheep.copy(headAngle = jumpTransitionData.headAngle),
            fluffColor = jumpTransitionData.color,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .scale(
                    scaleY = jumpTransitionData.sheepScale.y,
                    scaleX = jumpTransitionData.sheepScale.x
                )
                .align(Alignment.BottomCenter)
                .offset(y = jumpTransitionData.offsetY),
            glassesTranslation = jumpTransitionData.glassesTranslation,
        )
    }
}

@Composable
private fun updateJumpTransitionData(jumpState: SheepJumpState): JumpTransitionData {
    val transition = updateTransition(targetState = jumpState, label = "jumpSheepTransition")

    val offsetY = transition.animateDp(label = "jumpSheepTransitionOffsetY", transitionSpec = {
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
        )
    }) { state ->
        when (state) {
            Start -> 0.dp
            Crouch -> SheepCanvasSize.times(0.25f)
            Top -> -SheepJumpSize
            End -> SheepCanvasSize.times(0.1f)
        }
    }

    val color = transition.animateColor(label = "jumpSheepTransitionColor") { state ->
        when (state) {
            Start -> SheepColor.Gray
            Crouch -> SheepColor.Purple
            Top -> SheepColor.Green
            End -> SheepColor.Blue
        }
    }

    val headAngle =
        transition.animateFloat(label = "jumpSheepTransitionHeadAngle", transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
            )
        }) { state ->
            when (state) {
                Start -> 5f
                Crouch -> -10f
                Top -> -20f
                End -> 30f
            }
        }

    val glassesTranslation =
        transition.animateFloat(label = "jumpSheepTransitionGlassesTranslation", transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessHigh
            )
        }) { state ->
            when (state) {
                Start -> 0f
                Crouch -> 0f
                Top -> 0f
                End -> -70f
            }
        }

    val sheepScale =
        transition.animateOffset(label = "jumpSheepTransitionSheepScale", transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
            )
        }) { state ->
            when (state) {
                Start -> Offset(1f, 1f)
                Crouch -> Offset(1.1f, 0.75f)
                Top -> Offset(1f, 1f)
                End -> Offset(1.1f, 0.9f)
            }
        }

    return remember(transition) {
        JumpTransitionData(
            offsetY, color, headAngle, glassesTranslation, sheepScale
        )
    }
}

private fun SheepJumpState.getDelayAfter(): Long {
    return when (this) {
        Start -> 300
        Crouch -> 500
        Top -> 400
        End -> 300
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    TransitionsScreen()
}

@Preview(showBackground = true)
@Composable
private fun PreviewJumpingSheep() {
    JumpingSheep(
        jumpState = Start, sheepUiState = SheepUiState(), modifier = Modifier.fillMaxSize()
    )
}
