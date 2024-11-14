package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tasky.database.model.OfflineHistoryEntity

@Dao
interface OfflineHistoryDao {
    @Query("SELECT * FROM OfflineHistoryEntity")
    suspend fun getAll(): List<OfflineHistoryEntity>

    @Query("DELETE FROM OfflineHistoryEntity")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(entity: OfflineHistoryEntity)

    @Delete
    suspend fun delete(entity: OfflineHistoryEntity)
}
