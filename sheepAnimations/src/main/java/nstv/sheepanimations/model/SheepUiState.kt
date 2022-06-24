package nstv.sheepanimations.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import nstv.sheep.model.Sheep

val SheepCanvasSize = 250.dp
val SheepOriginalSize = DpSize(SheepCanvasSize, SheepCanvasSize)

private val SheepJumpSize = SheepCanvasSize

// Float value of the sheep jump size dp. Used for animations, meant to be used with .dp extension
val SheepJumpingOffset = -SheepJumpSize.value

data class SheepUiState(
    val sheep: Sheep = Sheep(),
    val sheepSize: DpSize = SheepOriginalSize,
    val sheepScale: Offset = Offset(1f, 1f),
    val isJumping: Boolean = false,
    val isGroovy: Boolean = false,
    val isBlinking: Boolean = false,
)
