package com.joao.otavio.identification_presentation.events

sealed class IdentificationEvents {
    data class OnTypingIdentification(val identificationNumber: String) : IdentificationEvents()
    object OnConfirmClick : IdentificationEvents()
}
