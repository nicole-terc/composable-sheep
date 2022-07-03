package nstv.sheepanimations.screens.gesture

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nstv.design.theme.SheepColor
import nstv.sheep.ComposableSheep
import nstv.sheepanimations.model.GesturesSheepUiState
import java.lang.Float.min
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun FlyingSheep(
    modifier: Modifier = Modifier,
) {
    val sheepUiState by remember {
        mutableStateOf(
            GesturesSheepUiState()
        )
    }

    val sheepColor = remember { Animatable(SheepColor.Blue) }

    Box(modifier = modifier.fillMaxSize()) {
        ComposableSheep(
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .align(Alignment.BottomCenter)
                .flyAwayAndRunBack { },
            sheep = sheepUiState.sheep,
            fluffColor = sheepColor.value,
        )
    }
}

fun Modifier.flyAwayAndRunBack(
    onAnimationEnded: () -> Unit
): Modifier = composed {
    val offsetY = remember { Animatable(0f) }
    val offsetX = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {

                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                val velocityTracker = VelocityTracker()

                offsetY.stop()
                offsetX.stop()
                rotation.stop()
                alpha.stop()
                scale.stop()

                awaitPointerEventScope {
                    drag(pointerId = pointerId) { change ->

                        launch {
                            offsetY.snapTo(offsetY.value + change.positionChange().y)
                            offsetX.snapTo(offsetX.value + change.positionChange().x)
                        }

                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetY = decay.calculateTargetValue(
                    offsetY.value, velocity
                )
                val targetOffsetX = decay.calculateTargetValue(
                    offsetX.value, velocity.div(4)
                )

                if (targetOffsetY.absoluteValue < size.height.div(4f)) {
                    // Not enough velocity; Slide back.
                    launch { offsetY.animateTo(targetValue = 0f, initialVelocity = velocity) }
                    launch { offsetX.animateTo(targetValue = 0f, initialVelocity = velocity) }
                    launch { rotation.animateTo(targetValue = 0f, initialVelocity = velocity) }
                    launch { scale.snapTo(1f) }
                    launch { alpha.snapTo(1f) }
                } else {
                    // Fly away!
                    val flyDuration = 700
                    val maxVerticalMove = size.height.times(4f)
                    val maxHorizontalMove = size.width.toFloat()
                    val angleToRotate = 360f.times(3f)
                    val easing = LinearOutSlowInEasing

                    val verticalMove =
                        min(targetOffsetY.absoluteValue + (size.height / 2), maxVerticalMove)

                    val horizontalMoveSign = if (targetOffsetX > 0) 1 else -1
                    val horizontalMove = min(
                        targetOffsetX.absoluteValue + (size.width / 2), maxHorizontalMove
                    ) * horizontalMoveSign

                    coroutineScope {
                        launch {
                            offsetY.animateTo(
                                -verticalMove,
                                initialVelocity = velocity,
                                animationSpec = tween(flyDuration, easing = easing)
                            )
                        }

                        launch {
                            offsetX.animateTo(
                                horizontalMove,
                                initialVelocity = velocity,
                                animationSpec = tween(flyDuration, easing = easing)
                            )
                        }

                        launch {
                            rotation.animateTo(
                                angleToRotate,
                                initialVelocity = velocity,
                                animationSpec = tween(flyDuration, easing = easing)
                            )
                            rotation.snapTo(0f)
                        }

                        launch {
                            scale.animateTo(
                                0f,
                                initialVelocity = velocity,
                                animationSpec = tween(flyDuration, easing = easing)
                            )
                        }

                        launch {
                            scale.animateTo(
                                0f,
                                initialVelocity = velocity,
                                animationSpec = tween(flyDuration, easing = easing)
                            )
                        }
                    }
                    delay(100)
                    offsetY.snapTo(0f)
                    offsetX.snapTo(0f)
                    rotation.snapTo(0f)
                    launch {
                        scale.animateTo(
                            1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                    }
                    launch {
                        alpha.animateTo(1f)
                    }
                    onAnimationEnded()
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
        .graphicsLayer {
            rotationZ = rotation.value
            scaleX = scale.value
            scaleY = scale.value
            this.alpha = alpha.value
        }
}
