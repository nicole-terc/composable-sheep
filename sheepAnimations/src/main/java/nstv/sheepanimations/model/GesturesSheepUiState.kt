package nstv.sheepanimations.model

import androidx.compose.ui.unit.DpSize
import nstv.sheep.model.Sheep

data class GesturesSheepUiState(
    val sheep: Sheep = Sheep(),
    val sheepSize: DpSize = SheepDefaultSize,
)
