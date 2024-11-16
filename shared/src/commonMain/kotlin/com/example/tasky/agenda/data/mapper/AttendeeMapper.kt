package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.RemoteAttendee
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.database.model.AttendeeSerialized

fun RemoteAttendee.toAttendee() =
    Attendee(
        email = email,
        fullName = fullName,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = remindAt,
    )

fun AttendeeSerialized.toAttendee() =
    Attendee(
        email = email,
        fullName = name,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = remindAt,
    )
