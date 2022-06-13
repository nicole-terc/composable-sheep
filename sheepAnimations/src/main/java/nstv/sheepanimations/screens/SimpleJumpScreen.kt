package nstv.sheepanimations.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import nstv.sheep.SheepComposable
import nstv.sheepanimations.model.SheepAnimationsUiState
import nstv.sheepanimations.model.SheepJumpSize

@Composable
fun SimpleJumpScreen(
    modifier: Modifier = Modifier,
) {
    val screenSize = DpSize(
        width = LocalConfiguration.current.screenWidthDp.dp,
        height = LocalConfiguration.current.screenHeightDp.dp
    )

    var uiState by remember { mutableStateOf(SheepAnimationsUiState(screenSize)) }

    val infiniteTransition = rememberInfiniteTransition()

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SheepComposable(
                sheep = uiState.sheep,
                modifier = Modifier
                    .size(uiState.sheepSize)
                    .offset(
                        x = uiState.topLeftPosition.width,
                        y = if (uiState.isJumping) uiState.topLeftPosition.height - SheepJumpSize * offsetY else uiState.topLeftPosition.height
                    )
                    .scale(scaleX = uiState.sheepScale.x, scaleY = uiState.sheepScale.y)

            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                uiState = uiState.copy(
                    isJumping = !uiState.isJumping
                )
            }
        ) {
            Text(text = "Sheep it!")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    SimpleMoveScreen()
}
