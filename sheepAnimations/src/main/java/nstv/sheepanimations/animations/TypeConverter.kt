package nstv.sheepanimations.animations

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val DpSizeConverter = TwoWayConverter<DpSize, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.width.value, it.height.value) },
    convertFromVector = { vector -> DpSize(vector.v1.dp, vector.v2.dp) }
)

val DpOffsetConverter = TwoWayConverter<DpOffset, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.x.value, it.y.value) },
    convertFromVector = { vector -> DpOffset(vector.v1.dp, vector.v2.dp) }
)

val OffsetConverter = TwoWayConverter<Offset, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.x, it.y) },
    convertFromVector = { vector -> Offset(vector.v1, vector.v2) }
)
