package com.example.tasky.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.tasky.agenda.domain.model.RemindAtType

data class EventWithAttendeeAndPhoto(
    @Embedded val eventEntity: EventEntity,
    @Relation(parentColumn = "id", entityColumn = "eventId")
    val attendees: List<AttendeeEntity>,
    @Relation(parentColumn = "id", entityColumn = "eventId")
    val photos: List<PhotoEntity>,
)

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
)

@Entity(
    primaryKeys = ["userId", "eventId"],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("eventId"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("eventId")],
)
data class AttendeeEntity(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("eventId"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("eventId")],
)
data class PhotoEntity(
    val eventId: String,
    @PrimaryKey val key: String,
    val url: String,
)
