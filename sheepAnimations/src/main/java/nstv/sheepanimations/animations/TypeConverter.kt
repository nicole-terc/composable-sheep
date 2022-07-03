package nstv.sheepanimations.animations

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val DpSizeConverter = TwoWayConverter<DpSize, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.width.value, it.height.value) },
    convertFromVector = { vector -> DpSize(vector.v1.dp, vector.v2.dp) }
)
