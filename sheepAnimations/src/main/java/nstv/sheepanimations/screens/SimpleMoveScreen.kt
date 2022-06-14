package nstv.sheepanimations.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.design.theme.TextUnit
import nstv.sheep.SheepComposable
import nstv.sheepanimations.model.SheepAnimationsUiState
import nstv.sheepanimations.model.SheepJumpingOffset

@Composable
fun SimpleMoveScreen(
    modifier: Modifier = Modifier,
) {
    var uiState by remember { mutableStateOf(SheepAnimationsUiState()) }
    val offsetY by animateDpAsState(
        targetValue =
        if (uiState.isJumping) SheepJumpingOffset.dp
        else 0.dp
    )

    Column(
        modifier = modifier
            .fillMaxSize()
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
                    .align(Alignment.BottomCenter)
                    .offset(y = offsetY)
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
            Text(text = "Sheep it!", fontWeight = FontWeight.Bold, fontSize = TextUnit.Twenty)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    SimpleMoveScreen()
}
