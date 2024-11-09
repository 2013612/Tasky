package com.example.tasky.database.mapper

import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.database.model.ReminderEntity

fun Reminder.toReminderEntity(): ReminderEntity =
    ReminderEntity(
        id = id,
        title = title,
        description = description,
        time = time,
        remindAt = remindAt,
    )
