package nstv.sheepcanvas.screens.canvasbasics

import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import nstv.sheep.extra.getSheepPathEffect

val pointModeOptions = listOf(
    "Points" to PointMode.Points,
    "Lines" to PointMode.Lines,
    "Polygon" to PointMode.Polygon,
)

val strokeCapOptions = listOf(
    "Butt" to StrokeCap.Butt,
    "Round" to StrokeCap.Round,
    "Square" to StrokeCap.Square,
)

val pathEffectOptions = listOf(
    "None" to null,
    "Corner" to PathEffect.cornerPathEffect(40f),
    "Dash" to PathEffect.dashPathEffect(floatArrayOf(40f, 40f)),
    "Stamped" to getSheepPathEffect(20f),
)

val drawStyleOptions = listOf(
    "Fill" to Fill,
    "Stroke" to Stroke(20f)
)

val strokeJoinOptions = listOf(
    "Miter" to StrokeJoin.Miter,
    "Round" to StrokeJoin.Round,
    "Bevel" to StrokeJoin.Bevel,
)
