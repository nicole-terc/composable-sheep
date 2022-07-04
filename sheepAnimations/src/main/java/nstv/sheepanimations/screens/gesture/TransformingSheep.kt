package nstv.sheepanimations.screens.gesture

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nstv.design.theme.SheepColor
import nstv.sheep.ComposableSheep
import nstv.sheepanimations.extensions.rotateBy
import nstv.sheepanimations.model.GesturesSheepUiState
import nstv.sheepanimations.model.SheepDefaultSize

@Composable
fun TransformingSheep(
    modifier: Modifier = Modifier,
) {
    val sheepUiState by remember {
        mutableStateOf(
            GesturesSheepUiState(
                sheepSize = SheepDefaultSize
            )
        )
    }

    var sheepOffset by remember { mutableStateOf(Offset.Zero) }
    val sheepColor = remember { Animatable(SheepColor.Green) }
    var sheepScale by remember { mutableStateOf(1f) }
    var sheepRotation by remember { mutableStateOf(0f) }

    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        ComposableSheep(
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    coroutineScope {

                        launch {
                            detectTransformGestures { centroid, pan, zoom, rotation ->
                                scope.launch {
                                    sheepColor.animateTo(SheepColor.Purple)
                                }
                                val oldScale = sheepScale
                                val newScale = sheepScale * zoom

                                val newOffset =
                                    ((sheepOffset + centroid / oldScale).rotateBy(rotation) - (centroid / newScale + pan / oldScale))

                                val newRotation = sheepRotation + rotation

                                sheepOffset = newOffset
                                sheepScale = newScale
                                sheepRotation = newRotation
                            }
                        }
                        launch {
                            forEachGesture {
                                awaitPointerEventScope {
                                    awaitFirstDown(requireUnconsumed = false)
                                    do {
                                        val pointerEvent = awaitPointerEvent()
                                    } while (pointerEvent.changes.any { it.pressed })
                                    scope.launch {
                                        sheepColor.animateTo(SheepColor.Green)
                                    }
                                }
                            }
                        }
                    }
                }
                .graphicsLayer {
                    scaleX = sheepScale
                    scaleY = sheepScale
                    rotationZ = sheepRotation
                    translationX = -sheepOffset.x * sheepScale
                    translationY = -sheepOffset.y * sheepScale
                    transformOrigin = TransformOrigin(0f, 0f)
                },
            sheep = sheepUiState.sheep,
            fluffColor = sheepColor.value,
        )
    }
}
