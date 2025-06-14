package com.joao.otavio.identification_presentation.ui.screens.identification

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.joao.otavio.design_system.buttons.WebTrackerButton
import com.joao.otavio.design_system.design.themes.DarkTheme
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.dimensions.Paddings
import com.joao.otavio.design_system.headers.HeaderCard
import com.joao.otavio.design_system.headers.LightHeader
import com.joao.otavio.design_system.outlinedTextField.WebTrackerOutlinedTextField
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun EmployeeIdentificationScreen(
    webTrackerTheme: WebTrackerTheme = WebTrackerTheme
) {
    val paddings = LocalPaddings.current

    WebTrackerScaffold(
        topBar = {
            EmployeeIdentificationTopAppBar(
                webTrackerTheme = webTrackerTheme
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
                webTrackerTheme = webTrackerTheme
            )
        }
    }
}

@Composable
private fun EmployeeIdentification(
    paddings: Paddings,
    webTrackerTheme: WebTrackerTheme
) {
    var employeeIdentification by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddings.xSmall),
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
            onClick = { },
            theme = webTrackerTheme,
        )
    }
}

@Composable
fun EmployeeIdentificationTopAppBar(webTrackerTheme: WebTrackerTheme) {
    Column(
        modifier = Modifier
            .background(webTrackerTheme.primary)
    ) {
        LightHeader(
            title = "Matricula do operador",
            theme = webTrackerTheme,
            onClickLeftIcon = rememberVectorPainter(Icons.Default.Settings),
            onClickLeft = {  }
        )

        HeaderCard(
            headerText = "Web Tracker Organization",
            webTrackerTheme = webTrackerTheme,
            onInitialTextClick = {  }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeIdentificationScreenPreview() {
    WebTrackerTheme {
        EmployeeIdentificationScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeIdentificationScreenDarkPreview() {
    WebTrackerTheme {
        EmployeeIdentificationScreen(webTrackerTheme = DarkTheme())
    }
}
