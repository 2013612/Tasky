package com.example.tasky.agenda.data.model

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/event")
class EventPath

@Serializable
data class UpdateEventBody(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>,
    val deletedPhotoKeys: List<String>,
    val isGoing: Boolean,
    val photos: List<ByteArray>,
) {
    constructor(event: Event, deletedPhotoKeys: List<String>, isGoing: Boolean, photos: List<ByteArray>) : this(
        id = event.id,
        title = event.title,
        description = event.description,
        from = event.from,
        to = event.to,
        remindAt = event.remindAt,
        attendeeIds = event.attendees.map { it.userId },
        deletedPhotoKeys = deletedPhotoKeys,
        isGoing = isGoing,
        photos = photos,
    )
}

data class CreateEventBody(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>,
    val photos: List<ByteArray>,
) {
    constructor(event: Event, photos: List<ByteArray>) : this(
        id = event.id,
        title = event.title,
        description = event.description,
        from = event.from,
        to = event.to,
        remindAt = event.remindAt,
        attendeeIds = event.attendees.map { it.userId },
        photos = photos,
    )
}
