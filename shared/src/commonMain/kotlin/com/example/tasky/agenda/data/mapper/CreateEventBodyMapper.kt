package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.CreateEventBody
import com.example.tasky.agenda.domain.model.Event
import kotlin.time.DurationUnit

fun Event.toCreateEventBody(): CreateEventBody =
    CreateEventBody(
        id = id,
        title = title,
        description = description,
        from = from,
        to = to,
        remindAt = from - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
        attendeeIds = attendees.map { it.userId },
    )
