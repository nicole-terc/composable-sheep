package nstv.sheepanimations.screens.gesture

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import kotlinx.coroutines.launch
import nstv.design.theme.SheepColor
import nstv.sheep.ComposableSheep
import nstv.sheepanimations.extensions.toIntOffset
import nstv.sheepanimations.model.GesturesSheepUiState
import nstv.sheepanimations.model.SheepDefaultSize


@Composable
fun DraggingSheep(
    modifier: Modifier = Modifier,
) {
    val sheepUiState by remember {
        mutableStateOf(
            GesturesSheepUiState(
                sheepSize = SheepDefaultSize.div(2f)
            )
        )
    }

    val sheepOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val sheepColor = remember { Animatable(SheepColor.Gray) }
    val sheepScale = remember { Animatable(1f) }

    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        ComposableSheep(
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .offset { sheepOffset.value.toIntOffset() }
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            awaitFirstDown()
                            scope.launch {
                                launch {
                                    sheepScale.animateTo(1.5f)
                                }
                                launch {
                                    sheepColor.animateTo(SheepColor.Red)
                                }
                            }
                            do {
                                val pointerEvent = awaitPointerEvent()
                                pointerEvent.changes.forEach { change ->
                                    scope.launch {
                                        // Pointer offset
                                        sheepOffset.snapTo(
                                            Offset(
                                                x = sheepOffset.value.x + change.positionChange().x,
                                                y = sheepOffset.value.y + change.positionChange().y
                                            )
                                        )
                                    }
                                }
                            } while (pointerEvent.changes.any { it.pressed })
                            scope.launch {
                                launch {
                                    sheepScale.animateTo(1f)
                                }
                                launch {
                                    sheepColor.animateTo(SheepColor.Gray)
                                }
                            }
                        }
                    }
                }
                .scale(sheepScale.value),
            sheep = sheepUiState.sheep,
            fluffColor = sheepColor.value,
        )
    }
}
