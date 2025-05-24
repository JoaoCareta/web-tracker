package com.joao.otavio.design_system.outlinedTextField

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.MainTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalAlpha
import com.joao.otavio.design_system.dimensions.LocalDimensions

@Composable
fun WebTrackerOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    theme: WebTrackerTheme = MainTheme(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {
    val dimensions = LocalDimensions.current
    val alpha = LocalAlpha.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = theme.secondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "$label Icon",
                tint = theme.secondary
            )
        },
        textStyle = TextStyle(color = theme.secondary),
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = theme.secondary,
            unfocusedBorderColor = theme.secondary,
            focusedContainerColor = theme.background,
            unfocusedContainerColor = theme.background,
            focusedTextColor = theme.secondary,
            unfocusedTextColor = theme.secondary,
            cursorColor = theme.secondary,
            disabledBorderColor = theme.secondary.copy(alpha = alpha.medium),
            disabledTextColor = theme.secondary.copy(alpha = alpha.medium),
            disabledLeadingIconColor = theme.secondary.copy(alpha = alpha.medium),
            disabledLabelColor = theme.secondary.copy(alpha = alpha.medium)
        )
    )

    Spacer(modifier = Modifier.height(dimensions.small))
}

@Preview(showBackground = true)
@Composable
fun PreviewWebTrackerOutlinedTextFieldMainTheme() {
    WebTrackerOutlinedTextField(
        value = "",
        onValueChange = {  },
        label = "Email",
        leadingIcon = Icons.Default.Email,
        theme = MainTheme(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF121212
)
@Composable
fun PreviewWebTrackerOutlinedTextFieldDarkTheme() {
    WebTrackerOutlinedTextField(
        value = "",
        onValueChange = {  },
        label = "Email",
        leadingIcon = Icons.Default.Email,
        theme = DarkTheme(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}
