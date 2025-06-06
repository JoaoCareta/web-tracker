package com.joao.otavio.authentication_domain.usecases

import com.joao.otavio.authentication_data.model.domain.Organization
import com.joao.otavio.authentication_presentation.datasource.AuthenticationLocalDataSource
import com.joao.otavio.authentication_presentation.usecases.SaveOrganizationUseCase
import javax.inject.Inject

class SaveOrganizationUseCaseImpl @Inject constructor(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
) : SaveOrganizationUseCase {
    override suspend fun invoke(organization: Organization): Boolean {
        return authenticationLocalDataSource.saveOrganizationInDatabase(organization) &&
            authenticationLocalDataSource.saveOrganizationInDataStore(organization)
    }
}
