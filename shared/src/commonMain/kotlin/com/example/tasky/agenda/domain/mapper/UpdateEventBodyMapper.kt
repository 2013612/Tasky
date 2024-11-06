package com.example.tasky.agenda.domain.mapper

import com.example.tasky.agenda.data.model.UpdateEventBody
import com.example.tasky.agenda.domain.model.Event
import kotlin.time.DurationUnit

fun Event.toUpdateEventBody(
    deletedPhotoKeys: List<String>,
    isGoing: Boolean,
): UpdateEventBody =
    UpdateEventBody(
        id = id,
        title = title,
        description = description,
        from = from,
        to = to,
        remindAt = from - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
        attendeeIds = attendees.map { it.userId },
        deletedPhotoKeys = deletedPhotoKeys,
        isGoing = isGoing,
    )
