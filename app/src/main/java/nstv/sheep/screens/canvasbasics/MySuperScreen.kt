package nstv.sheep.screens.canvasbasics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nstv.design.theme.ComposableSheepAnimationsTheme

@Composable
fun MySuperScreen() {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {

            // Draw line from top-left to bottom-right
            drawLine(
                color = Color.Magenta,
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 8.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(36.dp.toPx(), 8.dp.toPx())
                )
            )

//            // Draw rect from the middle of the canvas
//            drawRect(
//                brush = Brush.horizontalGradient(colors = listOf(Color.Blue, Color.Yellow)),
//                topLeft = size.center,
//                size = size.div(4f)
//            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ComposableSheepAnimationsTheme {
        MySuperScreen()
    }
}
