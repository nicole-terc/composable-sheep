package nstv.sheepanimations.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

fun Modifier.nonRippleClickable(onClick: () -> Unit) = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

fun DpSize.scaled(scale: Offset): DpSize {
    return DpSize(
        width = this.width * scale.x,
        height = this.height * scale.y
    )
}

fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())
