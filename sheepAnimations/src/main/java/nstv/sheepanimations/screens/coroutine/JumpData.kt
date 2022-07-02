package nstv.sheepanimations.screens.coroutine

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import nstv.design.theme.SheepColor
import nstv.sheepanimations.model.SheepUiState
import nstv.sheepanimations.screens.transition.SheepJumpState

data class JumpData(
    val offsetY: Dp,
    val scale: Offset,
    val color: Color,
    val alpha: Float,
    val headAngle: Float,
    val glassesTranslation: Float,
    val shadowSize: DpSize
)

fun getJumpDataValuesForStates(
    sheepUiState: SheepUiState,
): NonNullMap<SheepJumpState, JumpData> = NonNullMap(
    mapOf(
        SheepJumpState.Start to JumpData(
            offsetY = sheepUiState.sheepSize.height.times(0.05f),
            scale = Offset(1f, 1f),
            color = SheepColor.Gray,
            alpha = 1f,
            headAngle = 5f,
            glassesTranslation = 0f,
            shadowSize = DpSize(
                sheepUiState.sheepSize.width.times(0.6f), sheepUiState.sheepSize.height.times(0.2f)
            )
        ),
        SheepJumpState.Crouch to JumpData(
            offsetY = sheepUiState.sheepSize.height.times(0.2f),
            scale = Offset(1.1f, 0.75f),
            color = SheepColor.Purple,
            alpha = 1f,
            headAngle = -10f,
            glassesTranslation = 0f,
            shadowSize = DpSize(
                sheepUiState.sheepSize.width.times(0.7f), sheepUiState.sheepSize.height.times(0.2f)
            )
        ),
        SheepJumpState.Top to JumpData(
            offsetY = -sheepUiState.sheepJumpSize,
            scale = if (sheepUiState.isScaling) Offset(1.5f, 1.5f) else Offset(1f, 1f),
            color = SheepColor.Green,
            alpha = 0.5f,
            headAngle = -20f,
            glassesTranslation = 0f,
            shadowSize = DpSize(
                sheepUiState.sheepSize.width.times(0.3f), sheepUiState.sheepSize.height.times(0.1f)
            )
        ),
        SheepJumpState.End to JumpData(
            offsetY = sheepUiState.sheepSize.height.times(0.15f),
            scale = Offset(1.1f, 0.9f),
            color = SheepColor.Blue,
            alpha = 1f,
            headAngle = 30f,
            glassesTranslation = -70f,
            shadowSize = DpSize(
                sheepUiState.sheepSize.width.times(0.7f), sheepUiState.sheepSize.height.times(0.2f)
            )
        )
    )
)

fun <T> SheepJumpState.getAnimationSpecForOffsetTo(): FiniteAnimationSpec<T> = when (this) {
    SheepJumpState.Start -> spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium
    )
    SheepJumpState.Crouch -> spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )
    SheepJumpState.Top -> spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    SheepJumpState.End -> spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
}

class NonNullMap<K, V>(private val map: Map<K, V>) : Map<K, V> by map {
    override operator fun get(key: K): V {
        return map[key]!! // Force an NPE if the key doesn't exist
    }
}
