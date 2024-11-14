package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.database.model.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    suspend fun getById(id: String): TaskEntity

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM TaskEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM TaskEntity WHERE time >= :startTime AND time < :endTime")
    suspend fun getByTime(
        startTime: Long,
        endTime: Long,
    ): List<TaskEntity>
}
