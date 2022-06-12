package nstv.sheepanimations.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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

@Composable
fun SimpleMoveScreen(
    modifier: Modifier = Modifier,
) {
    val screenSize = DpSize(
        width = LocalConfiguration.current.screenWidthDp.dp,
        height = LocalConfiguration.current.screenHeightDp.dp
    )

    var uiState by remember { mutableStateOf(SheepAnimationsUiState(screenSize)) }
    val offsetY by animateDpAsState(targetValue = uiState.topLeftPosition.height)

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SheepComposable(
                sheep = uiState.sheep,
                modifier = Modifier
                    .size(uiState.sheepSize)
                    .aspectRatio(1f)
                    .offset(
                        x = uiState.topLeftPosition.width,
                        y = offsetY
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
