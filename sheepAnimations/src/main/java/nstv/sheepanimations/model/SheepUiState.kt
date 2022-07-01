package nstv.sheepanimations.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import nstv.sheep.model.Sheep

val SheepCanvasSize = 250.dp
val SheepOriginalSize = DpSize(SheepCanvasSize, SheepCanvasSize)

val SheepJumpSize = SheepCanvasSize

// Float value of the sheep jump size dp. Used for animations, meant to be used with .dp extension
val SheepJumpingOffset = -SheepJumpSize.value

data class SheepUiState(
    val sheep: Sheep = Sheep(),
    val sheepSize: DpSize = SheepOriginalSize,
    val sheepJumpSize: Dp = sheepSize.height,
    val isJumping: Boolean = false,
    val isGroovy: Boolean = false,
    val isBlinking: Boolean = false,
    val isScaling: Boolean = false,
    val isAppearing: Boolean = false,
    val isHeadBanging: Boolean = false,
    val movingGlasses: Boolean = false,
    val hasShadow: Boolean = false,
    val animationsEnabled: Boolean = false,
) {
    val hasAnimations get() = isJumping || isGroovy || isBlinking || isScaling || isHeadBanging
    val isAnimating = animationsEnabled && hasAnimations
}

fun SheepUiState.withGroovyJump() = copy(
    isJumping = true,
    isGroovy = true,
    isHeadBanging = true,
    movingGlasses = true,
    hasShadow = true
)
