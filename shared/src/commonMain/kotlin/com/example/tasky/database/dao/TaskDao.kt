package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.database.model.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    fun getById(id: String): TaskEntity

    @Upsert
    fun upsert(task: TaskEntity)

    @Delete
    fun delete(task: TaskEntity)

    @Query("DELETE FROM TaskEntity")
    fun deleteAll()
}
