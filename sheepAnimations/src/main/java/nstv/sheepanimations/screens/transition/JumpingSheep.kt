package nstv.sheepanimations.screens.transition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nstv.canvasExtensions.nextItemLoop
import nstv.design.theme.SheepColor
import nstv.sheep.ComposableSheep
import nstv.sheep.model.DefaultHeadRotationAngle
import nstv.sheep.model.Sheep
import nstv.sheepanimations.model.SheepDefaultSize
import nstv.sheepanimations.model.SheepUiState
import nstv.sheepanimations.model.withGroovyJump
import nstv.sheepanimations.screens.transition.SheepJumpState.Start

@Composable
fun JumpingSheep(
    modifier: Modifier = Modifier,
    sheep: Sheep = Sheep(),
    size: DpSize = SheepDefaultSize,
    jumping: Boolean = true,
) {
    val sheepUiState = SheepUiState(sheep = sheep, sheepSize = size).withGroovyJump()
    JumpingSheep(sheepUiState = sheepUiState, modifier = modifier, jumping = jumping)
}

@Composable
fun JumpingSheep(
    sheepUiState: SheepUiState,
    modifier: Modifier = Modifier,
    jumping: Boolean = true,
) {
    var jumpState by remember { mutableStateOf(Start) }

    LaunchedEffect(jumping) {
        launch {
            while (jumping) {
                jumpState =
                    SheepJumpState.values().nextItemLoop(SheepJumpState.values().indexOf(jumpState))
                delay(jumpState.getDelayAfter())
            }
            jumpState = Start
        }
    }

    JumpingSheep(sheepUiState = sheepUiState, jumpState = jumpState, modifier = modifier)
}

@Composable
fun JumpingSheep(
    sheepUiState: SheepUiState,
    jumpState: SheepJumpState,
    modifier: Modifier = Modifier,
) {
    val jumpTransitionData = updateJumpTransitionData(sheepUiState, jumpState)

    Box(
        modifier = modifier.height(sheepUiState.sheepJumpSize + sheepUiState.sheepSize.height)
    ) {

        if (sheepUiState.hasShadow) {
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
            sheep = sheepUiState.sheep.copy(headAngle = if (sheepUiState.isHeadBanging) jumpTransitionData.headAngle else DefaultHeadRotationAngle),
            fluffColor = if (sheepUiState.isGroovy) jumpTransitionData.color else SheepColor.Green,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .scale(
                    scaleX = jumpTransitionData.scale.x,
                    scaleY = jumpTransitionData.scale.y
                )
                .align(Alignment.BottomCenter)
                .offset(y = jumpTransitionData.offsetY)
                .alpha(if (sheepUiState.isBlinking) jumpTransitionData.alpha else 1f),
            glassesTranslation = if (sheepUiState.movingGlasses) jumpTransitionData.glassesTranslation else 0f,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewJumpingSheep() {
    JumpingSheep(
        sheepUiState = SheepUiState(), jumpState = Start, modifier = Modifier.fillMaxSize()
    )
}
