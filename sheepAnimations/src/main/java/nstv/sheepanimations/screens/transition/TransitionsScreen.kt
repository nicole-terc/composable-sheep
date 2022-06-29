package nstv.sheepanimations.screens.transition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nstv.canvasExtensions.nextItemLoop
import nstv.design.theme.SheepColor
import nstv.design.theme.TextUnit
import nstv.design.theme.components.StartStopBehaviorButton
import nstv.sheep.ComposableSheep
import nstv.sheep.model.DefaultHeadRotationAngle
import nstv.sheepanimations.model.SheepCanvasSize
import nstv.sheepanimations.model.SheepJumpSize
import nstv.sheepanimations.model.SheepUiState
import nstv.sheepanimations.screens.transition.SheepJumpState.Start

private const val StepByStep = false
private const val Groovy = true
private const val MovingGlasses = true
private const val HeadBanging = true
private const val WithShadow = true
private const val Appearing = false

@Composable
fun TransitionsScreen(
    modifier: Modifier = Modifier,
) {
    val verticalScroll = rememberScrollState()
    var sheepUiState by remember { mutableStateOf(SheepUiState()) }
    var jumpState by remember { mutableStateOf(Start) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(sheepUiState) {
        if (!StepByStep) {
            launch {
                if (Appearing) {
                    isVisible = true
                    delay(500)
                }
                while (sheepUiState.animationsEnabled) {
                    jumpState =
                        SheepJumpState.values()
                            .nextItemLoop(SheepJumpState.values().indexOf(jumpState))
                    delay(jumpState.getDelayAfter())
                }
                jumpState = Start
                isVisible = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .animateContentSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = if (Appearing) isVisible else true,
                enter = scaleIn(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(),
                exit = slideOutHorizontally { fullWidth -> -fullWidth.times(1.2).toInt() },
            ) {
                JumpingSheep(jumpState = jumpState, sheepUiState = sheepUiState)
            }
        }
        StartStopBehaviorButton(
            isBehaviorActive = sheepUiState.animationsEnabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            onClick = {
                if (StepByStep) {
                    jumpState = SheepJumpState.values()
                        .nextItemLoop(SheepJumpState.values().indexOf(jumpState))
                } else {
                    sheepUiState =
                        sheepUiState.copy(animationsEnabled = !sheepUiState.animationsEnabled)
                }
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

        if (WithShadow) {
            Box(
                modifier = Modifier
                    .size(jumpTransitionData.shadowSize)
                    .align(Alignment.BottomCenter)
                    .drawBehind {
                        drawOval(color = SheepColor.Black.copy(0.5f))
                    }
            )
        }

        ComposableSheep(
            sheep = sheepUiState.sheep.copy(headAngle = if (HeadBanging) jumpTransitionData.headAngle else DefaultHeadRotationAngle),
            fluffColor = if (Groovy) jumpTransitionData.color else SheepColor.Green,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .scale(
                    scaleY = jumpTransitionData.sheepScale.y,
                    scaleX = jumpTransitionData.sheepScale.x
                )
                .align(Alignment.BottomCenter)
                .offset(y = jumpTransitionData.offsetY)
                .alpha(jumpTransitionData.alpha),
            glassesTranslation = if (MovingGlasses) jumpTransitionData.glassesTranslation else 0f,
        )
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
