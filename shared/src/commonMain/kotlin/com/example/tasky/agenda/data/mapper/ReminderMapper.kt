package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.RemoteReminder
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.getRemindAtType
import com.example.tasky.database.model.ReminderEntity

fun RemoteReminder.toReminder(): Reminder =
    Reminder(
        id = id,
        title = title,
        description = description,
        time = time,
        remindAt = getRemindAtType(time, remindAt),
    )

fun ReminderEntity.toReminder(): Reminder =
    Reminder(
        id = id,
        title = title,
        description = description,
        time = time,
        remindAt = remindAt,
    )
