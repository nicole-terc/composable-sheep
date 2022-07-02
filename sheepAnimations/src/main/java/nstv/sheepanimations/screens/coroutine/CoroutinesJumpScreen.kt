package nstv.sheepanimations.screens.coroutine

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.VectorConverter
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nstv.canvasExtensions.nextItemLoop
import nstv.design.theme.SheepColor
import nstv.design.theme.TextUnit
import nstv.design.theme.components.StartStopBehaviorButton
import nstv.sheep.ComposableSheep
import nstv.sheep.model.DefaultHeadRotationAngle
import nstv.sheepanimations.animations.DpSizeConverter
import nstv.sheepanimations.model.SheepUiState
import nstv.sheepanimations.screens.transition.SheepJumpState
import nstv.sheepanimations.screens.transition.SheepJumpState.Start

private const val StepByStep = false
private const val Groovy = true
private const val Scaling = true
private const val Blinking = true
private const val MovingGlasses = true
private const val HeadBanging = true
private const val HasShadow = true
private const val Appearing = false

@Composable
fun CoroutinesJumpScreen(
    modifier: Modifier = Modifier,
) {
    val verticalScroll = rememberScrollState()
    var sheepUiState by remember {
        mutableStateOf(
            SheepUiState(
                isGroovy = Groovy,
                isScaling = Scaling,
                isBlinking = Blinking,
                isHeadBanging = HeadBanging,
                movingGlasses = MovingGlasses,
                hasShadow = HasShadow,
                isAppearing = Appearing,
            )
        )
    }
    val jumpDataMap = remember { getJumpDataValuesForStates(sheepUiState = sheepUiState) }

    var isVisible by remember { mutableStateOf(false) }
    var jumpState by remember { mutableStateOf(Start) }
    var jumpData by remember { mutableStateOf(jumpDataMap[Start]) }
    val colorConverter = remember(jumpData.color.colorSpace) {
        (Color.VectorConverter)(jumpData.color.colorSpace)
    }

    // Easy way to get color
    val color = remember { Animatable(jumpData.color) }

    LaunchedEffect(sheepUiState.animationsEnabled) {
        if (!StepByStep) {
            launch {
                if (Appearing) {
                    isVisible = true
                    delay(500)
                }
                while (sheepUiState.animationsEnabled) {

                    for (currentStepName in SheepJumpState.values()) {
                        coroutineScope {
                            val nextStepName = SheepJumpState.values().nextItemLoop(currentStepName)

                            val currentStep = jumpDataMap[currentStepName]
                            val nextStep = jumpDataMap[nextStepName]

                            // OffsetY
                            launch {
                                animate(
                                    initialValue = currentStep.offsetY.value,
                                    targetValue = nextStep.offsetY.value,
                                    animationSpec = nextStepName.getAnimationSpecForOffsetTo()
                                ) { value, _ ->
                                    jumpData = jumpData.copy(offsetY = value.dp)
                                }
                            }

                            // scale
                            launch {
                                animate(
                                    typeConverter = Offset.VectorConverter,
                                    initialValue = currentStep.scale,
                                    targetValue = nextStep.scale,
                                    animationSpec = nextStepName.getAnimationSpecForOffsetTo()
                                ) { value, _ ->
                                    jumpData = jumpData.copy(scale = value)
                                }
                            }

                            // shadowSize
                            if (sheepUiState.hasShadow)
                                launch {
                                    animate(
                                        typeConverter = DpSizeConverter,
                                        initialValue = currentStep.shadowSize,
                                        targetValue = nextStep.shadowSize,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    ) { value, _ ->
                                        jumpData = jumpData.copy(shadowSize = value)
                                    }
                                }

                            // color
                            if (sheepUiState.isGroovy)
                                launch {
                                    animate(
                                        typeConverter = colorConverter,
                                        initialValue = currentStep.color,
                                        targetValue = nextStep.color
                                    ) { value, _ ->
                                        jumpData = jumpData.copy(color = value)
                                    }
                                    // easy way to get color
                                    // color.animateTo(nextStep.color)
                                    // jumpData = jumpData.copy(color = nextStep.color)
                                }

                            // alpha
                            if (sheepUiState.isBlinking)
                                launch {
                                    animate(
                                        initialValue = currentStep.alpha,
                                        targetValue = nextStep.alpha
                                    ) { value, _ ->
                                        jumpData = jumpData.copy(alpha = value)
                                    }
                                }

                            // headAngle
                            if (sheepUiState.isHeadBanging)
                                launch {
                                    animate(
                                        initialValue = currentStep.headAngle,
                                        targetValue = nextStep.headAngle,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    ) { value, _ ->
                                        jumpData = jumpData.copy(headAngle = value)
                                    }
                                }

                            // glassesTranslation
                            if (sheepUiState.movingGlasses)
                                launch {
                                    animate(
                                        initialValue = currentStep.glassesTranslation,
                                        targetValue = nextStep.glassesTranslation,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    ) { value, _ ->
                                        jumpData = jumpData.copy(glassesTranslation = value)
                                    }
                                }
                            jumpState = nextStepName
                        }
                    }
                }

                jumpState = Start
                jumpData = jumpDataMap[Start]
                isVisible = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .animateContentSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = if (sheepUiState.isAppearing) isVisible else true,
                enter = scaleIn(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(),
                exit = slideOutHorizontally { fullWidth -> -fullWidth.times(1.2).toInt() },
            ) {
                Box(
                    modifier = modifier.height(sheepUiState.sheepJumpSize + sheepUiState.sheepSize.height)
                ) {

                    if (sheepUiState.hasShadow) {
                        Box(
                            modifier = Modifier
                                .size(jumpData.shadowSize)
                                .align(Alignment.BottomCenter)
                                .drawBehind {
                                    drawOval(color = SheepColor.Black.copy(0.5f))
                                }
                        )
                    }

                    ComposableSheep(
                        sheep = sheepUiState.sheep.copy(headAngle = if (sheepUiState.isHeadBanging) jumpData.headAngle else DefaultHeadRotationAngle),
                        fluffColor = if (sheepUiState.isGroovy) jumpData.color else SheepColor.Green,
                        modifier = Modifier
                            .size(sheepUiState.sheepSize)
                            .scale(
                                scaleX = jumpData.scale.x, scaleY = jumpData.scale.y
                            )
                            .align(Alignment.BottomCenter)
                            .offset(y = jumpData.offsetY)
                            .alpha(if (sheepUiState.isBlinking) jumpData.alpha else 1f),
                        glassesTranslation = if (sheepUiState.movingGlasses) jumpData.glassesTranslation else 0f,
                    )
                }
            }
        }
        StartStopBehaviorButton(
            isBehaviorActive = sheepUiState.animationsEnabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            onClick = {
                if (StepByStep) {
                    jumpData = jumpDataMap[
                        SheepJumpState.values()
                            .nextItemLoop(SheepJumpState.values().indexOf(jumpState))
                    ]
                } else {
                    sheepUiState =
                        sheepUiState.copy(animationsEnabled = !sheepUiState.animationsEnabled)
                }
            }
        ) {
            val text = if (sheepUiState.animationsEnabled) "Shtop it!" else "Sheep it!"
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = TextUnit.Twenty)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheepAnimation() {
    CoroutinesJumpScreen()
}
