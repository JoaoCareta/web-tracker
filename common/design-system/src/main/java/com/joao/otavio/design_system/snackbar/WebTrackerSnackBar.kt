package com.joao.otavio.design_system.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalPaddings
import kotlinx.coroutines.delay


@Composable
fun WebTrackerSnackBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String? = null,
    subtitle: String? = null,
    iconId: Int,
    backgroundColor: Color,
    textColor: Color,
    iconColor: Color,
    duration: Int = SnackbarDuration.Long.ordinal,
    onDismiss: () -> Unit,
) {
    val paddings = LocalPaddings.current
    val dimensions = LocalDimensions.current
    val startTime = rememberSaveable { mutableStateOf(0L) }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddings.xSmall)
        ) {
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensions.xxxSmall),
                color = backgroundColor,
                tonalElevation = dimensions.mini
            ) {
                Row(
                    modifier = modifier
                        .padding(paddings.xSmall)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xxxSmall)
                ) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        tint = iconColor
                    )

                    Column {
                        title?.let {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor
                            )
                        }

                        subtitle?.let {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(visible) {
        if (visible && duration != SnackbarDuration.Indefinite.ordinal) {
            if (startTime.value == 0L) {
                startTime.value = System.currentTimeMillis()
            }

            val elapsedTime = System.currentTimeMillis() - startTime.value
            val snackBarDuration = when (duration) {
                SnackbarDuration.Short.ordinal -> SNACK_BAR_DURATION
                else -> SNACK_BAR_LONG_DURATION
            }

            val remainingTime = (snackBarDuration - elapsedTime).coerceAtLeast(0)

            if (remainingTime > 0) {
                delay(remainingTime)
                onDismiss()
                startTime.value = 0L
            }
        }
    }
}

const val SNACK_BAR_DURATION = 4000L
const val SNACK_BAR_LONG_DURATION = 7000L
