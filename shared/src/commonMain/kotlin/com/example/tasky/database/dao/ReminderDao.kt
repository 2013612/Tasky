package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.database.model.ReminderEntity

@Dao
interface ReminderDao {
    @Query("SELECT * FROM ReminderEntity WHERE id = :id")
    suspend fun getById(id: String): ReminderEntity

    @Upsert
    suspend fun upsert(reminder: ReminderEntity)

    @Delete
    suspend fun delete(reminder: ReminderEntity)

    @Query("DELETE FROM ReminderEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM ReminderEntity WHERE time >= :startTime AND time < :endTime")
    suspend fun getByTime(
        startTime: Long,
        endTime: Long,
    ): List<ReminderEntity>
}
