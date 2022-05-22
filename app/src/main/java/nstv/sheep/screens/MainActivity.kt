package nstv.sheep.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nstv.design.theme.ComposableSheepAnimationsTheme
import nstv.sheep.R
import nstv.sheep.screens.sheepscreen.SheepViewerScreen

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableSheepAnimationsTheme {

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
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        SmallTopAppBar(
                            title = { Text(text = getString(R.string.app_name)) }
                        )
                    }
                ) {
                    SheepViewerScreen()
                }
            }
        }
    }
}
