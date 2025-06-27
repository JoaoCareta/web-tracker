package com.joao.otavio.design_system.mapper

import com.google.gson.Gson
import com.joao.otavio.authentication_data.model.entity.Organization as OrganizationEntity
import com.joao.otavio.authentication_data.model.domain.Organization as OrganizationDomain
import com.joao.otavio.authentication_data.mapper.OrganizationMapper.toDomain
import com.joao.otavio.authentication_data.mapper.OrganizationMapper.toEntity
import org.junit.Test
import java.util.UUID
import junit.framework.TestCase.assertEquals

class OrganizationMapperTest {
    private val gson = Gson()

    @Test
    fun `when OrganizationEntity is mapped to Domain then fields should match`() {
        // Mockk
        val organizationEntity = createOrganizationEntity()

        // Run Test
        val organizationDomain = organizationEntity.toDomain()

        // Assert
        assertEquals(gson.toJson(organizationDomain), gson.toJson(organizationEntity))
    }

    @Test
    fun `when OrganizationDomain is mapped to Entity then fields should match`() {
        // Mockk
        val organizationDomain = createOrganizationDomain()

        // Run Test
        val organizationEntity = organizationDomain.toEntity()

        // Assert
        assertEquals(gson.toJson(organizationDomain), gson.toJson(organizationEntity))
    }

    @Test
    fun `when entity is mapped to domain and back to entity then it should be identical`() {
        // Mockk
        val originalEntity = createOrganizationEntity()

        // Run Test
        val convertedToDomain = originalEntity.toDomain()
        val convertedBackToEntity = convertedToDomain.toEntity()

        // Assert
        assertEquals(gson.toJson(originalEntity), gson.toJson(convertedToDomain))
        assertEquals(gson.toJson(originalEntity), gson.toJson(convertedBackToEntity))
    }

    @Test
    fun `when domain is mapped to entity and back to domain then it should be identical`() {
        // Mockk (não aplicável diretamente para mappers sem dependências, mas mantido para padrão)
        val originalDomain = createOrganizationDomain()

        // Run Test
        val convertedToEntity = originalDomain.toEntity()
        val convertedBackToDomain = convertedToEntity.toDomain()

        // Assert
        assertEquals(gson.toJson(originalDomain), gson.toJson(convertedToEntity))
        assertEquals(gson.toJson(originalDomain), gson.toJson(convertedBackToDomain))
    }

    private fun createOrganizationEntity() : OrganizationEntity {
        return OrganizationEntity(
            organizationName = ORGANIZATION_UUID,
            organizationUuid = ORGANIZATION_NAME
        )
    }

    private fun createOrganizationDomain(): OrganizationDomain {
        return OrganizationDomain(
            organizationUuid = ORGANIZATION_UUID,
            organizationName = ORGANIZATION_NAME
        )
    }

    companion object {
        val ORGANIZATION_UUID = UUID.randomUUID().toString()
        const val ORGANIZATION_NAME = "organization_name"
    }
}