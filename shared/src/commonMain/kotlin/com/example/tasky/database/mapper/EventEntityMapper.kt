package com.example.tasky.database.mapper

import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.database.model.AttendeeSerialized
import com.example.tasky.database.model.EventEntity
import com.example.tasky.database.model.PhotoSerialized

fun Event.toEventEntity(): EventEntity =
    EventEntity(
        id = id,
        title = title,
        description = description,
        from = from,
        to = to,
        remindAt = remindAt,
        host = host,
        isUserEventCreator = isUserEventCreator,
        attendees = attendees.map { it.toAttendeeSerialized() },
        photos = photos.map { it.toPhotoSerialized() },
    )

fun Attendee.toAttendeeSerialized(): AttendeeSerialized =
    AttendeeSerialized(
        email = email,
        name = fullName,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = remindAt,
    )

fun Photo.toPhotoSerialized(): PhotoSerialized =
    PhotoSerialized(
        key = key,
        url = url,
    )
