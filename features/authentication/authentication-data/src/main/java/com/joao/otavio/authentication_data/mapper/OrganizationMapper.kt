package com.joao.otavio.authentication_data.mapper

import com.joao.otavio.authentication_data.model.entity.Organization as OrganizationEntity
import com.joao.otavio.authentication_data.model.domain.Organization as OrganizationDomain
import com.joao.otavio.utils.mapper.Mapper

object OrganizationMapper : Mapper<OrganizationEntity, OrganizationDomain> {
    override fun OrganizationEntity.toDomain(): OrganizationDomain {
        return OrganizationDomain(
            organizationId = this.organizationUuid,
            organizationName = this.organizationName
        )
    }

    override fun OrganizationDomain.toEntity(): OrganizationEntity {
        return OrganizationEntity(
            organizationUuid = this.organizationId,
            organizationName = this.organizationName
        )
    }
}
