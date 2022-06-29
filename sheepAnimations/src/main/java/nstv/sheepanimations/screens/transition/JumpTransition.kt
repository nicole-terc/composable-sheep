package nstv.sheepanimations.screens.transition

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import nstv.design.theme.Grid
import nstv.design.theme.SheepColor
import nstv.sheepanimations.animations.DpSizeConverter
import nstv.sheepanimations.model.SheepCanvasSize
import nstv.sheepanimations.model.SheepJumpSize

enum class SheepJumpState {
    Start, Crouch, Top, End,
}

class JumpTransitionData(
    offsetY: State<Dp>,
    color: State<Color>,
    alpha: State<Float>,
    headAngle: State<Float>,
    glassesTranslation: State<Float>,
    sheepScale: State<Offset>,
    shadowSize: State<DpSize>,
) {
    val offsetY by offsetY
    val color by color
    val alpha by alpha
    val headAngle by headAngle
    val glassesTranslation by glassesTranslation
    val sheepScale by sheepScale
    val shadowSize by shadowSize
}

@Composable
fun updateJumpTransitionData(jumpState: SheepJumpState): JumpTransitionData {
    val transition = updateTransition(targetState = jumpState, label = "jumpSheepTransition")

    val offsetY = transition.animateDp(label = "jumpSheepTransitionOffsetY", transitionSpec = {
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
        )
    }) { state ->
        when (state) {
            SheepJumpState.Start -> SheepCanvasSize.times(0.05f)
            SheepJumpState.Crouch -> SheepCanvasSize.times(0.2f)
            SheepJumpState.Top -> -SheepJumpSize
            SheepJumpState.End -> SheepCanvasSize.times(0.1f)
        }
    }

    val color = transition.animateColor(label = "jumpSheepTransitionColor") { state ->
        when (state) {
            SheepJumpState.Start -> SheepColor.Gray
            SheepJumpState.Crouch -> SheepColor.Purple
            SheepJumpState.Top -> SheepColor.Green
            SheepJumpState.End -> SheepColor.Blue
        }
    }

    val alpha = transition.animateFloat(label = "jumpSheepTransitionAlpha") { state ->
        when (state) {
            SheepJumpState.Start -> 1f
            SheepJumpState.Crouch -> 1f
            SheepJumpState.Top -> 0.5f
            SheepJumpState.End -> 1f
        }
    }

    val headAngle =
        transition.animateFloat(label = "jumpSheepTransitionHeadAngle", transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
            )
        }) { state ->
            when (state) {
                SheepJumpState.Start -> 5f
                SheepJumpState.Crouch -> -10f
                SheepJumpState.Top -> -20f
                SheepJumpState.End -> 30f
            }
        }

    val glassesTranslation =
        transition.animateFloat(label = "jumpSheepTransitionGlassesTranslation", transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh
            )
        }) { state ->
            when (state) {
                SheepJumpState.Start -> 0f
                SheepJumpState.Crouch -> 0f
                SheepJumpState.Top -> 0f
                SheepJumpState.End -> -70f
            }
        }

    val sheepScale =
        transition.animateOffset(label = "jumpSheepTransitionSheepScale", transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
            )
        }) { state ->
            when (state) {
                SheepJumpState.Start -> Offset(1f, 1f)
                SheepJumpState.Crouch -> Offset(1.1f, 0.75f)
                SheepJumpState.Top -> Offset(1.5f, 1.5f)
                SheepJumpState.End -> Offset(1.1f, 0.9f)
            }
        }

    val shadowSize = transition.animateValue(
        label = "jumpSheepTransitionShadowSize",
        transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow)
        },
        typeConverter = DpSizeConverter
    ) { state ->
        when (state) {
            SheepJumpState.Start -> DpSize(SheepCanvasSize.times(0.6f), Grid.Five)
            SheepJumpState.Crouch -> DpSize(SheepCanvasSize.times(0.7f), Grid.Six)
            SheepJumpState.Top -> DpSize(SheepCanvasSize.times(0.3f), Grid.Two)
            SheepJumpState.End -> DpSize(SheepCanvasSize.times(0.7f), Grid.Five)
        }
    }

    return remember(transition) {
        JumpTransitionData(
            offsetY, color, alpha, headAngle, glassesTranslation, sheepScale, shadowSize
        )
    }
}

fun SheepJumpState.getDelayAfter(): Long {
    return when (this) {
        SheepJumpState.Start -> 300
        SheepJumpState.Crouch -> 500
        SheepJumpState.Top -> 400
        SheepJumpState.End -> 300
    }
}
