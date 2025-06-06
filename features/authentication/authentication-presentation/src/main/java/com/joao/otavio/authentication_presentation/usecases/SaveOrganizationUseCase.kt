package com.joao.otavio.authentication_presentation.usecases

import com.joao.otavio.authentication_data.model.domain.Organization

fun interface SaveOrganizationUseCase {
    suspend operator fun invoke(organization: Organization) : Boolean
}
