package nstv.sheepanimations.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import nstv.design.theme.SheepColor
import nstv.design.theme.TextUnit
import nstv.sheep.SheepComposable
import nstv.sheepanimations.model.SheepUiState

@Composable
fun SimpleColorScreen(
    modifier: Modifier = Modifier,
) {
    var sheepUiState by remember { mutableStateOf(SheepUiState()) }

    val color by animateColorAsState(
        targetValue = if (sheepUiState.isGroovy) SheepColor.Green else SheepColor.Gray,
        animationSpec = tween(durationMillis = 250)
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {

        SheepComposable(
            sheep = sheepUiState.sheep,
            fluffColor = color,
            modifier = Modifier
                .size(sheepUiState.sheepSize)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                sheepUiState = sheepUiState.copy(isGroovy = !sheepUiState.isGroovy)
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
