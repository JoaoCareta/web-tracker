package com.joao.otavio.design_system.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalFontSize
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun LocationPermissionDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    webTrackerTheme: WebTrackerTheme = WebTrackerTheme
) {
    val isChecked = remember { mutableStateOf(false) }
    val fontSize = LocalFontSize.current
    val paddings = LocalPaddings.current
    val dimensions = LocalDimensions.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = webTrackerTheme.background,
                shape = RoundedCornerShape(paddings.xSmall)
            )
            .padding(paddings.large)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_location_permission),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensions.xxLarge)
                )

                Spacer(modifier = Modifier.width(dimensions.xSmall))

                Text(
                    text = stringResource(id = R.string.popup_location_permission_description),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = fontSize.xSmall,
                        color = webTrackerTheme.primaryText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(dimensions.xSmall))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = paddings.xxHuge)
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    modifier = Modifier.padding(end = paddings.xxxSmall),
                    colors = CheckboxDefaults.colors(
                        checkedColor = webTrackerTheme.secondary,
                        uncheckedColor = webTrackerTheme.outline,
                        checkmarkColor = Color.White,
                    )
                )

                Text(
                    text = stringResource(id = R.string.popup_location_permission_checkbox),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = fontSize.xSmall,
                        color = webTrackerTheme.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(dimensions.xSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = webTrackerTheme.secondaryButtonEnabled
                    ),
                    modifier = Modifier
                        .height(dimensions.xxLarge)
                        .padding(end = paddings.xHuge)
                ) {
                    Text(
                        text = stringResource(id = R.string.popup_permission_cancel),
                        fontSize = fontSize.xSmall,
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        color = webTrackerTheme.primaryText
                    )
                }

                Button(
                    onClick = onConfirm,
                    enabled = isChecked.value,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isChecked.value)
                            webTrackerTheme.secondary
                        else
                            webTrackerTheme.secondaryButtonEnabled
                    ),
                    modifier = Modifier.height(dimensions.xxLarge)
                ) {
                    Text(
                        text = stringResource(id = R.string.popup_permission_confirm),
                        fontSize = fontSize.xSmall,
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        color = if (isChecked.value)
                            webTrackerTheme.secondaryText
                        else
                            webTrackerTheme.primaryText
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LocationPermissionDialogPreview() {
    LocationPermissionDialog(
        onConfirm =  {},
        onCancel = {}
    )
}
