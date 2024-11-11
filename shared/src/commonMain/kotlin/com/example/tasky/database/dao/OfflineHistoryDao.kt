package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tasky.database.model.OfflineHistoryEntity

@Dao
interface OfflineHistoryDao {
    @Query("SELECT * FROM OfflineHistoryEntity")
    fun getAll(): List<OfflineHistoryEntity>

    @Query("DELETE FROM OfflineHistoryEntity")
    fun deleteAll()

    @Insert
    fun insert(entity: OfflineHistoryEntity)

    @Delete
    fun delete(entity: OfflineHistoryEntity)
}
