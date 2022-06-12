package nstv.sheep.sheep.extra

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import nstv.sheep.sheep.model.FluffStyle
import nstv.sheep.sheep.parts.getFluffPath

fun getSheepPathEffect(
    miniFluffRadius: Float
) =
    PathEffect.stampedPathEffect(
        shape = getFluffPath(
            circleCenterOffset = Offset.Zero,
            circleRadius = miniFluffRadius,
            fluffStyle = FluffStyle.Random()
        ),
        advance = miniFluffRadius * 3f,
        phase = miniFluffRadius,
        style = StampedPathEffectStyle.Morph
    )
