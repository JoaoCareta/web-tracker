package com.joao.otavio.authentication_data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joao.otavio.utils.constants.DatabaseConstants

@Entity(tableName = DatabaseConstants.Organization.TABLE_NAME)
data class Organization(
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstants.Organization.ORGANIZATION_UUID)
    val organizationUuid: String,

    @ColumnInfo(name = DatabaseConstants.Organization.ORGANIZATION_NAME)
    val organizationName: String
)
