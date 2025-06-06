package com.joao.otavio.design_system.mapper

import com.joao.otavio.authentication_data.model.entity.Organization as OrganizationEntity
import com.joao.otavio.authentication_data.model.domain.Organization as OrganizationDomain
import com.joao.otavio.authentication_data.mapper.OrganizationMapper.toDomain
import com.joao.otavio.authentication_data.mapper.OrganizationMapper.toEntity
import org.junit.Test
import java.util.UUID
import junit.framework.TestCase.assertEquals

class OrganizationMapperTest {
    @Test
    fun `when OrganizationEntity is mapped to Domain then fields should match`() {
        // Mockk
        val entityId = UUID.randomUUID().toString()
        val entityName = "Test Organization Entity"
        val organizationEntity = OrganizationEntity(
            organizationUuid = entityId,
            organizationName = entityName
        )

        // Run Test
        val organizationDomain = organizationEntity.toDomain()

        // Assert
        assertEquals(entityId, organizationDomain.organizationId)
        assertEquals(entityName, organizationDomain.organizationName)
    }

    @Test
    fun `when OrganizationDomain is mapped to Entity then fields should match`() {
        // Mockk
        val domainId = UUID.randomUUID().toString()
        val domainName = "Test Organization Domain"
        val organizationDomain = OrganizationDomain(
            organizationId = domainId,
            organizationName = domainName
        )

        // Run Test
        val organizationEntity = organizationDomain.toEntity()

        // Assert
        assertEquals(domainId, organizationEntity.organizationUuid)
        assertEquals(domainName, organizationEntity.organizationName)
    }

    @Test
    fun `when entity is mapped to domain and back to entity then it should be identical`() {
        // Mockk
        val originalEntity = OrganizationEntity(
            organizationUuid = UUID.randomUUID().toString(),
            organizationName = "Original Entity Name"
        )

        // Run Test
        val convertedToDomain = originalEntity.toDomain()
        val convertedBackToEntity = convertedToDomain.toEntity()

        // Assert
        assertEquals(originalEntity.organizationUuid, convertedBackToEntity.organizationUuid)
        assertEquals(originalEntity.organizationName, convertedBackToEntity.organizationName)
    }

    @Test
    fun `when domain is mapped to entity and back to domain then it should be identical`() {
        // Mockk (não aplicável diretamente para mappers sem dependências, mas mantido para padrão)
        val originalDomain = OrganizationDomain(
            organizationId = UUID.randomUUID().toString(),
            organizationName = "Original Domain Name"
        )

        // Run Test
        val convertedToEntity = originalDomain.toEntity()
        val convertedBackToDomain = convertedToEntity.toDomain()

        // Assert
        assertEquals(originalDomain.organizationId, convertedBackToDomain.organizationId)
        assertEquals(originalDomain.organizationName, convertedBackToDomain.organizationName)
    }
}