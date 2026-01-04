
package com.example.jquiz

import android.os.Bundle
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jquiz.data.Knowledge
import com.example.jquiz.screens.MainScreen
import com.example.jquiz.services.Color
import com.example.jquiz.services.Font
import com.example.jquiz.services.PreferencesManager
import com.example.jquiz.services.createChannel
import com.example.jquiz.ui.theme.BlueDarkScheme
import com.example.jquiz.ui.theme.BlueLightScheme
import com.example.jquiz.ui.theme.Christmas
import com.example.jquiz.ui.theme.DarkColorScheme
import com.example.jquiz.ui.theme.GreenDarkScheme
import com.example.jquiz.ui.theme.GreenLightScheme
import com.example.jquiz.ui.theme.JQuizTheme
import com.example.jquiz.ui.theme.LightColorScheme
import com.example.jquiz.ui.theme.Saira
import com.example.jquiz.ui.theme.Tagesschrift
import com.example.jquiz.ui.theme.Typography
import com.example.jquiz.ui.theme.typography
import com.example.jquiz.viewmodels.preferencesDatastore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            Knowledge.getDB(applicationContext)
        }
        createChannel(this)
        enableEdgeToEdge()
        setContent {
            JQuizTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        this,
                        { MainScreen() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JQuizTheme {
        Greeting("JQuiz")
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun App (context: Context, content: @Composable () -> Unit, modifier: Modifier = Modifier) {

    val preferencesManager = PreferencesManager(context.preferencesDatastore)

    val darkTheme by preferencesManager.getDarkThemeFlow().collectAsState(initial = false)
    val color by preferencesManager.getColorFlow().collectAsState(initial = Color.SYSTEM)
    val font by preferencesManager.getFontFlow().collectAsState(initial = Font.SYSTEM)

    val typography = when (font) {
        Font.TAGESSCHRIFT -> typography(Tagesschrift)
        Font.SAIRA -> typography(Saira)
        Font.CHRISTMAS -> typography(Christmas)
        Font.SYSTEM -> Typography
    }

    val theme = when (color) {
        Color.BLUE -> if (darkTheme) BlueDarkScheme else BlueLightScheme
        Color.GREEN -> if (darkTheme) GreenDarkScheme else GreenLightScheme
        Color.SYSTEM -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = theme,
        typography = typography,
        content = content
    )
}
