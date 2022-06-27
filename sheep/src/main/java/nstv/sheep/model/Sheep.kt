package nstv.sheep.model

import androidx.compose.ui.graphics.Color
import nstv.design.theme.SheepColor

const val DefaultHeadRotationAngle = 5f

data class Sheep(
    val fluffStyle: FluffStyle = FluffStyle.Random(),
    val headAngle: Float = DefaultHeadRotationAngle,
    val glassesTranslation: Float = 0f,
    val legs: List<Leg> = twoLegsStraight(),
    val fluffColor: Color = SheepColor.Fluff,
    val headColor: Color = SheepColor.Skin,
    val legColor: Color = headColor,
    val eyeColor: Color = SheepColor.Eye,
    val glassesColor: Color = SheepColor.Black
)
