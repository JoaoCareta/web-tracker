package com.joao.otavio.utils.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

@Dao
abstract class BaseUpsertDao<T> {

    /**
     * Insert the given [entity] if not exist yet
     *
     * @return id of affected row or [NO_ROW_AFFECTED] if already exist
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(entity: T): Long

    /**
     * Insert the given [entities] if not exist yet
     *
     * @return id of affected row or [NO_ROW_AFFECTED] if already exist
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(entities: List<T>): List<Long>

    /**
     * Update the given [entity] if it already exist
     *
     * @return number of affected rows
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(entity: T): Int

    /**
     * Update the given [entities] if it already exist
     *
     * @return number of affected rows
     */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun updateAll(entities: List<T>): Int

    /**
     * Insert or update the given [entity]
     *
     * @return `true` if succeed to insert/update
     */
    @Transaction
    open fun upsert(entity: T): Boolean {
        return if (insert(entity) == NO_ROW_AFFECTED) {
            update(entity) > 0
        } else {
            true
        }
    }

    /**
     * Insert or update the given [entities]
     *
     * @return `true` if succeed to insert/update all
     */
    @Transaction
    open fun upsertAll(entities: List<T>): Boolean {
        val entitiesToUpdate = insertAll(entities).mapIndexedNotNull { i, rowId ->
            entities[i].takeIf { rowId == NO_ROW_AFFECTED }
        }

        return if (entitiesToUpdate.isNotEmpty()) {
            updateAll(entitiesToUpdate) == entitiesToUpdate.size
        } else {
            true
        }
    }

    /**
     * Delete the given [entity]
     */
    @Delete
    abstract fun delete(entity: T)

    /**
     * Delete all the given [entities]
     */
    @Delete
    abstract fun deleteAll(entities: List<T>)

    companion object {
        const val NO_ROW_AFFECTED = -1L
    }
}
