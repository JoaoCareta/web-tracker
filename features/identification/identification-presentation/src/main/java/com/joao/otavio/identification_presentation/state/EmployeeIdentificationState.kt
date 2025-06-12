package com.joao.otavio.identification_presentation.state

import kotlinx.coroutines.flow.MutableStateFlow

data class EmployeeIdentificationState(
    val organizationName: MutableStateFlow<String> = MutableStateFlow(""),
    val employeeIdentificationNumber: MutableStateFlow<String> = MutableStateFlow(""),
)
