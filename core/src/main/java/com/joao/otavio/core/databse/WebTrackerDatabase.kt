package com.joao.otavio.core.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joao.otavio.authentication_data.database.OrganizationDao
import com.joao.otavio.authentication_data.model.entity.Organization
import com.joao.otavio.core.databse.WebTrackerDatabase.Companion.WEB_TRACKER_DATABASE_CURRENT_VERSION

@Database(
    version = WEB_TRACKER_DATABASE_CURRENT_VERSION,
    entities = [
        Organization::class
    ],
    autoMigrations = [],
    exportSchema = true,
)
abstract class WebTrackerDatabase : RoomDatabase() {

    abstract fun organizationDao() : OrganizationDao

    companion object {
        private const val DATABASE_VERSION_1 = 1
        const val WEB_TRACKER_DATABASE_CURRENT_VERSION = DATABASE_VERSION_1
        const val DATABASE_NAME = "web_tracker_database_name"
    }
}
