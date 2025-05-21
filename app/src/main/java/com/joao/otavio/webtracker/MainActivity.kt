package com.joao.otavio.webtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.*
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.headers.LightHeader
import com.joao.otavio.design_system.scaffold.MOCKK_TEXT
import com.joao.otavio.design_system.scaffold.WEB_TRACKER
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.webtracker.ui.theme.WebTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebTrackerTheme {
                WebTrackerScaffold(
                    topBar = {
                        LightHeader(
                            title = WEB_TRACKER,
                            onClickLeft = {}
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .background(WebTrackerTheme.background)
                    ) {
                        Text(
                            text = MOCKK_TEXT,
                            modifier = Modifier.align(Alignment.Center),
                            color = WebTrackerTheme.primaryText
                        )
                    }
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
            .padding(LocalPaddings.current.small)
            .background(WebTrackerTheme.background),
        color = WebTrackerTheme.primaryText,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebTrackerTheme {
        Greeting("Android-test-with-firebase-testers")
    }
}
