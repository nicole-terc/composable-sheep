package nstv.sheepanimations.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nstv.design.theme.ComposableSheepTheme
import nstv.design.theme.Grid
import nstv.design.theme.darkScrim
import nstv.design.theme.lightScrim
import nstv.sheepanimations.screens.canvas.CircleAnimationScreen
import nstv.sheepanimations.screens.coroutine.CoroutinesJumpScreen
import nstv.sheepanimations.screens.gesture.ChasingSheep
import nstv.sheepanimations.screens.gesture.DraggingSheep
import nstv.sheepanimations.screens.gesture.FlyingSheep
import nstv.sheepanimations.screens.gesture.TransformingSheep
import nstv.sheepanimations.screens.transition.TransitionsScreen

private enum class Screen {
    SIMPLE_SIZE,
    SIMPLE_COLOR,
    GROOVY_COLOR,
    SIMPLE_VISIBILITY,
    BLINK_VISIBILITY,
    SIMPLE_MOVE,
    SIMPLE_JUMP,
    ALL_IN_CHAOS,
    TRANSITIONS_JUMP,
    COROUTINES_JUMP,
    CHASING_SHEEP_GESTURES,
    DRAGGING_SHEEP_GESTURES,
    FLYING_SHEEP_GESTURES,
    TRANSFORMING_SHEEP_GESTURES,
    CIRCLE_ANIMATION,
}

class AnimationsMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableSheepTheme {
                val darkTheme = isSystemInDarkTheme()

                DisposableEffect(darkTheme) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT,
                        ) { darkTheme },
                        navigationBarStyle = SystemBarStyle.auto(
                            lightScrim,
                            darkScrim,
                        ) { darkTheme },
                    )
                    onDispose {}
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { paddingValues ->
                    var expanded by remember { mutableStateOf(false) }
                    var selectedScreen by remember { mutableStateOf(Screen.CHASING_SHEEP_GESTURES) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(Grid.Two)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopStart)
                        ) {
                            Row(modifier = Modifier.clickable { expanded = true }) {
                                Text(
                                    text = selectedScreen.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Screen"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                Screen.values().forEach { screen ->
                                    DropdownMenuItem(
                                        text = { Text(text = screen.name) },
                                        onClick = {
                                            expanded = false
                                            selectedScreen = screen
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(vertical = Grid.One))

                        Crossfade(
                            targetState = selectedScreen,
                            animationSpec = tween(durationMillis = 500)
                        ) { screen ->
                            when (screen) {
                                Screen.SIMPLE_SIZE -> SimpleSizeScreen()
                                Screen.SIMPLE_COLOR -> SimpleColorScreen()
                                Screen.GROOVY_COLOR -> GroovySheepScreen()
                                Screen.SIMPLE_VISIBILITY -> SimpleVisibilityScreen()
                                Screen.BLINK_VISIBILITY -> BlinkSheepScreen()
                                Screen.SIMPLE_MOVE -> SimpleMoveScreen()
                                Screen.SIMPLE_JUMP -> SimpleJumpScreen()
                                Screen.ALL_IN_CHAOS -> AllInChaosScreen()
                                Screen.TRANSITIONS_JUMP -> TransitionsScreen()
                                Screen.COROUTINES_JUMP -> CoroutinesJumpScreen()
                                Screen.CHASING_SHEEP_GESTURES -> ChasingSheep()
                                Screen.DRAGGING_SHEEP_GESTURES -> DraggingSheep()
                                Screen.FLYING_SHEEP_GESTURES -> FlyingSheep()
                                Screen.TRANSFORMING_SHEEP_GESTURES -> TransformingSheep()
                                Screen.CIRCLE_ANIMATION -> CircleAnimationScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
