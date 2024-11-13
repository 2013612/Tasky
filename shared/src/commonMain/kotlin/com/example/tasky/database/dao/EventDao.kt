package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.tasky.database.model.EventEntity

@Dao
interface EventDao {
    @Transaction
    @Query("SELECT * FROM EventEntity WHERE id = :id")
    fun getById(id: String): EventEntity

    @Upsert
    fun upsertEvent(event: EventEntity)

    @Delete
    fun delete(event: EventEntity)

    @Query("DELETE FROM EventEntity")
    fun deleteAll()
}
