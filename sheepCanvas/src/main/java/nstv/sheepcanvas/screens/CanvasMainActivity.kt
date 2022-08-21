package nstv.sheepcanvas.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nstv.design.theme.ComposableSheepTheme
import nstv.design.theme.Grid
import nstv.sheepcanvas.screens.canvasbasics.ArcScreen
import nstv.sheepcanvas.screens.canvasbasics.LineScreen
import nstv.sheepcanvas.screens.canvasbasics.MySuperScreen
import nstv.sheepcanvas.screens.canvasbasics.PointsScreen
import nstv.sheepcanvas.screens.canvasbasics.QuadraticBezierScreen
import nstv.sheepcanvas.screens.canvasbasics.ShapeScreen
import nstv.sheepcanvas.screens.sheepscreen.BasicSheepScreen
import nstv.sheepcanvas.screens.sheepscreen.SheepViewerScreen

private enum class Screen {
    SHEEP, BASIC_SHEEP, LINE, POINTS, SHAPE, ARC, SUPER, BEZIER
}

class CanvasMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableSheepTheme {

                val systemUiController = rememberSystemUiController()
                val useDarkIcons = isSystemInDarkTheme().not()
                val surfaceColor = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = surfaceColor,
                        darkIcons = useDarkIcons
                    )
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    var selectedScreen by remember { mutableStateOf(Screen.SHEEP) }

                    Column(
                        Modifier
                            .fillMaxSize()
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

                        when (selectedScreen) {
                            Screen.SHEEP -> SheepViewerScreen()
                            Screen.LINE -> LineScreen()
                            Screen.POINTS -> PointsScreen()
                            Screen.SHAPE -> ShapeScreen()
                            Screen.ARC -> ArcScreen()
                            Screen.SUPER -> MySuperScreen()
                            Screen.BASIC_SHEEP -> BasicSheepScreen()
                            Screen.BEZIER -> QuadraticBezierScreen()
                        }
                    }
                }
            }
        }
    }
}
