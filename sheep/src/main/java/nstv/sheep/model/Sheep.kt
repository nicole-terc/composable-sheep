package nstv.sheep.model

import androidx.compose.ui.graphics.Color

private const val DefaultHeadRotationAngle = 5f

data class Sheep(
    val fluffStyle: FluffStyle = FluffStyle.Random(),
    val headAngle: Float = DefaultHeadRotationAngle,
    val legs: List<Leg> = fourLegs(),
    val fluffColor: Color = Color.LightGray,
    val headColor: Color = Color.DarkGray,
    val legColor: Color = headColor,
    val glassesColor: Color = Color.Black
)
