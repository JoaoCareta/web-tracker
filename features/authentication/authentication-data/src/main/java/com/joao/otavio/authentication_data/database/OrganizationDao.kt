package com.joao.otavio.authentication_data.database

import androidx.room.Dao
import androidx.room.Query
import com.joao.otavio.authentication_data.model.entity.Organization
import com.joao.otavio.utils.database.BaseUpsertDao
import com.joao.otavio.utils.constants.DatabaseConstants

@Dao
abstract class OrganizationDao : BaseUpsertDao<Organization>() {

    @Query(
        "DELETE FROM ${DatabaseConstants.Organization.TABLE_NAME} " +
            "WHERE ${DatabaseConstants.Organization.ORGANIZATION_UUID} = :organizationId"
    )
    abstract fun deleteOrganizationById(organizationId: String): Int

    @Query("DELETE FROM ${DatabaseConstants.Organization.TABLE_NAME}")
    abstract fun deleteAllEntities(): Int
}
