package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.database.model.ReminderEntity

@Dao
interface ReminderDao {
    @Query("SELECT * FROM ReminderEntity WHERE id = :id")
    fun getById(id: String): ReminderEntity

    @Upsert
    fun upsert(reminder: ReminderEntity)

    @Delete
    fun delete(reminder: ReminderEntity)

    @Query("DELETE FROM ReminderEntity")
    fun deleteAll()
}
