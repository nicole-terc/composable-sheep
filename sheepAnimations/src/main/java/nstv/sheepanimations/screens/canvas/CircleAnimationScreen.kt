package nstv.sheepanimations.screens.canvas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nstv.design.theme.Grid

@Composable
fun CircleAnimationScreen(modifier: Modifier = Modifier) {
    val startAngle = remember { Animatable(-90f) }
    val sweepAngle = remember { Animatable(0f) }

    val startAngle2 = remember { Animatable(-90f) }
    val sweepAngle2 = remember { Animatable(0f) }

    val startAngle3 = remember { Animatable(-90f) }
    val sweepAngle3 = remember { Animatable(0f) }

    val runAnimation = remember { mutableStateOf(false) }

    LaunchedEffect(runAnimation.value) {
        if (runAnimation.value) {
            coroutineScope {
                launch {
                    startAngle.animateTo(
                        270f,
                        animationSpec = keyframes {
                            durationMillis = 1500
                            -90f at 0 with FastOutLinearInEasing
                            130f at 900 with LinearOutSlowInEasing
                        }
                    )
                }

                launch {
                    sweepAngle.animateTo(
                        0f,
                        animationSpec = keyframes {
                            durationMillis = 1500
                            10f at 0 with FastOutLinearInEasing
                            120f at 100 with LinearOutSlowInEasing
                            130f at 900 with LinearOutSlowInEasing
                        }
                    )
                }
                launch {
                    startAngle2.animateTo(
                        270f,
                        animationSpec = keyframes {
                            durationMillis = 1500
                            -80f at 0 with LinearOutSlowInEasing
                            130f at 700 with LinearOutSlowInEasing
                        }
                    )
                }

                launch {
                    sweepAngle2.animateTo(
                        0f,
                        animationSpec = keyframes {
                            durationMillis = 1500
                            10f at 0 with LinearOutSlowInEasing
                            100f at 100 with LinearOutSlowInEasing
                            130f at 700 with LinearOutSlowInEasing
                        }
                    )
                }
                launch {
                    startAngle3.animateTo(
                        270f,
                        animationSpec = keyframes {
                            durationMillis = 1500
                            -50f at 0 with LinearEasing
                            130f at 700 with LinearOutSlowInEasing
                        }
                    )
                }

                launch {
                    sweepAngle3.animateTo(
                        0f,
                        animationSpec = keyframes {
                            durationMillis = 1500
                            10f at 0 with LinearEasing
                            100f at 100 with LinearOutSlowInEasing
                            130f at 700 with LinearOutSlowInEasing
                        }
                    )
                }
            }
            runAnimation.value = false
        }
    }

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            drawArc(
                color = Color.Blue,
                startAngle = startAngle.value,
                sweepAngle = sweepAngle.value,
                useCenter = false,
                style = Stroke(
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    width = Grid.Two.toPx()
                )
            )

            drawArc(
                color = Color.Yellow,
                startAngle = startAngle2.value,
                sweepAngle = sweepAngle2.value,
                useCenter = false,
                style = Stroke(
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    width = Grid.Two.toPx()
                )
            )

            drawArc(
                color = Color.Green,
                startAngle = startAngle3.value,
                sweepAngle = sweepAngle3.value,
                useCenter = false,
                style = Stroke(
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    width = Grid.Two.toPx()
                )
            )
        }

        Button(
            onClick = { runAnimation.value = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = !runAnimation.value
        ) {
            Text("Start")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CircleAnimationScreenPreview() {
    CircleAnimationScreen()
}
