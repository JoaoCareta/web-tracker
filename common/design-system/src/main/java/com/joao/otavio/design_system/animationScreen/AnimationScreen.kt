package com.joao.otavio.design_system.animationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.joao.otavio.core.util.TimeUtils.HALF_SECOND
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalFontSize
import kotlinx.coroutines.delay

@Composable
fun SuccessAnimationScreen(
    animationResId: Int,
    onAnimationEnd: () -> Unit,
    animatedText: String,
    animatedTextColor: Color,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
    val fontSize = LocalFontSize.current
    val dimensions = LocalDimensions.current

    var isPlaying by remember { mutableStateOf(true) }
    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = 2,
        isPlaying = isPlaying,
        speed = 4.0f
    )

    LaunchedEffect(animationState.isAtEnd && composition != null) {
        if (animationState.isAtEnd && isPlaying) {
            isPlaying = false
            delay(HALF_SECOND)
            onAnimationEnd()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.size(138.dp),
            factory = { context ->
                LottieAnimationView(context).apply {
                    setAnimation(animationResId)
                    playAnimation()
                }
            },
            update = { it.run {} }
        )

        Text(
            text = animatedText,
            fontSize = fontSize.small,
            color = animatedTextColor,
            modifier = Modifier.padding(top = dimensions.small),
        )
    }
}
