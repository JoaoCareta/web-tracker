package com.joao.otavio.identification_presentation.ui.screens.identification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.navigation.NavigationEvent
import com.joao.otavio.design_system.buttons.WebTrackerButton
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.dimensions.Paddings
import com.joao.otavio.design_system.headers.WebTrackerTopBarHandler
import com.joao.otavio.design_system.outlinedTextField.WebTrackerOutlinedTextField
import com.joao.otavio.design_system.permissions.PermissionsHandler
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.utils.click.ClickUtils.doIfCanClick
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun EmployeeIdentificationScreen(
    webTrackerTheme: WebTrackerTheme = WebTrackerTheme,
    navigation: (NavigationEvent.Navigate) -> Unit,
) {
    val paddings = LocalPaddings.current
    val context = LocalContext.current

    PermissionsHandler(
        navigation = navigation,
        context = context
    )

    WebTrackerScaffold(
        topBar = {
            EmployeeIdentificationTopAppBar(
                webTrackerTheme = webTrackerTheme,
                onClickLeft = {
                    Toast.makeText(context, "Engine click", Toast.LENGTH_SHORT).show()
                }
            )
        },
        contentColor = webTrackerTheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(webTrackerTheme.background)
        ) {
            EmployeeIdentification(
                paddings = paddings,
                webTrackerTheme = webTrackerTheme,
                onConfirmClick = {
                    navigation.invoke(
                        WebTrackerScreens.WebTrackerMap.navigateReplacingStack()
                    )
                }
            )
        }
    }
}

@Composable
private fun EmployeeIdentification(
    paddings: Paddings,
    webTrackerTheme: WebTrackerTheme,
    onConfirmClick: () -> Unit
) {
    var employeeIdentification by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = paddings.xSmall,
                end = paddings.xSmall
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WebTrackerOutlinedTextField(
            value = employeeIdentification,
            onValueChange = { newNumber ->
                employeeIdentification = newNumber
            },
            label = stringResource(R.string.employee_identification_type_employee_number),
            leadingIcon = Icons.Default.AccountCircle,
            theme = webTrackerTheme,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            useSpacer = false,
        )
        WebTrackerButton(
            text = stringResource(R.string.employee_identification_confirm),
            onClick = onConfirmClick,
            theme = webTrackerTheme,
        )
    }
}

@Composable
fun EmployeeIdentificationTopAppBar(
    webTrackerTheme: WebTrackerTheme,
    onClickLeft: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(webTrackerTheme.primary)
    ) {
        WebTrackerTopBarHandler(
            title = "Matricula do operador",
            theme = webTrackerTheme,
            onClickLeftIcon = rememberVectorPainter(Icons.Default.Settings),
            onClickLeft = onClickLeft,
            headerText = "Web Tracker Organization",
            onInitialTextClick = {
                doIfCanClick {
                    Toast.makeText(context, "Organization click", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeIdentificationScreenPreview() {
    WebTrackerTheme {
        EmployeeIdentificationScreen(
            navigation = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeIdentificationScreenDarkPreview() {
    WebTrackerTheme {
        EmployeeIdentificationScreen(webTrackerTheme = DarkTheme(), navigation = {})
    }
}
