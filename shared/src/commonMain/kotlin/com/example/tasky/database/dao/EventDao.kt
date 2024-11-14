package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.database.model.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity WHERE id = :id")
    suspend fun getById(id: String): EventEntity

    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM EventEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM EventEntity WHERE `from` >= :startTime AND `from` < :endTime")
    suspend fun getByTime(
        startTime: Long,
        endTime: Long,
    ): List<EventEntity>
}
