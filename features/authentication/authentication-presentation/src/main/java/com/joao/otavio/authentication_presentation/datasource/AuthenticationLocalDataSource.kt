package com.joao.otavio.authentication_presentation.datasource

import com.joao.otavio.authentication_data.model.domain.Organization

interface AuthenticationLocalDataSource {
    suspend fun saveOrganizationInDatabase(organization: Organization): Boolean
    suspend fun saveOrganizationInDataStore(organizationId: Organization): Boolean
    suspend fun getOrganizationIdInDataStore(): String?
}
