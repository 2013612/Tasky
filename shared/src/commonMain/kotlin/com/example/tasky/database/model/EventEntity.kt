package com.example.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.agenda.domain.model.RemindAtType
import kotlinx.serialization.Serializable

@Entity
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: RemindAtType,
    val host: String,
    val isUserEventCreator: Boolean,
    val photos: List<PhotoSerialized>,
    val attendees: List<AttendeeSerialized>,
)

@Serializable
data class AttendeeSerialized(
    val userId: String,
    val email: String,
    val name: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
    val isCreator: Boolean,
)

@Serializable
data class PhotoSerialized(
    val key: String,
    val url: String,
)
