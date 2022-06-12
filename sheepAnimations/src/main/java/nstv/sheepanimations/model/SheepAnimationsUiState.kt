package nstv.sheepanimations.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import nstv.sheep.model.Sheep
import nstv.sheepanimations.extensions.scaled

val SheepCanvasSize = 200.dp
val SheepOriginalSize = DpSize(SheepCanvasSize, SheepCanvasSize)

private val SheepJumpSize = SheepCanvasSize

data class SheepAnimationsUiState(
    val screenSize: DpSize,
    val sheep: Sheep = Sheep(),
    val sheepSize: DpSize = SheepOriginalSize,
    val sheepScale: Offset = Offset(1f, 1f),
    val sheepCenterPosition: DpSize = DpSize(
        screenSize.width / 2,
        screenSize.height - sheepSize.height
    ),
    val isJumping: Boolean = false,
) {
    val topLeftPosition: DpSize
        get() {
            var position = sheepCenterPosition - sheepSize.scaled(sheepScale) / 2

            if (isJumping) {
                position -= DpSize(0.dp, SheepJumpSize)
            }

            return position
        }
}
