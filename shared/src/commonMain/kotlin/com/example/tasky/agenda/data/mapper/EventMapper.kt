package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.RemoteEvent
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.getRemindAtType
import com.example.tasky.database.model.EventEntity

fun RemoteEvent.toEvent() =
    Event(
        id = id,
        title = title,
        description = description,
        from = from,
        to = to,
        remindAt = getRemindAtType(from, remindAt),
        host = host,
        isUserEventCreator = isUserEventCreator,
        attendees = attendees.map { it.toAttendee() },
        photos = photos.map { it.toPhoto() },
    )

fun EventEntity.toEvent() =
    Event(
        id = id,
        title = title,
        description = description,
        from = from,
        to = to,
        remindAt = remindAt,
        host = host,
        isUserEventCreator = isUserEventCreator,
        attendees = attendees.map { it.toAttendee() },
        photos = photos.map { it.toPhoto() },
    )
