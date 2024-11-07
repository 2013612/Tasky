package com.example.tasky.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.tasky.database.model.AttendeeEntity
import com.example.tasky.database.model.EventEntity
import com.example.tasky.database.model.EventWithAttendeeAndPhoto
import com.example.tasky.database.model.PhotoEntity

@Dao
interface EventDao {
    @Transaction
    @Query("SELECT * FROM EventEntity WHERE id = :id")
    fun getById(id: String): EventWithAttendeeAndPhoto

    @Upsert
    fun upsertEvent(event: EventEntity)

    @Upsert
    fun upsertAttendees(attendees: List<AttendeeEntity>)

    @Upsert
    fun upsertPhotos(photos: List<PhotoEntity>)

    @Delete
    fun delete(event: EventEntity)
}
