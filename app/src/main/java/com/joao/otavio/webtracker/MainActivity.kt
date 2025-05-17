package com.joao.otavio.webtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.*
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.webtracker.ui.theme.WebTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebTrackerTheme {
                Scaffold() { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WebTrackerTheme.primaryIcon)
                            .padding(innerPadding)
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
