package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.RemoteReminder
import com.example.tasky.agenda.domain.model.Reminder
import kotlin.time.DurationUnit

fun Reminder.toRemoteReminder(): RemoteReminder =
    RemoteReminder(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = time - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
    )
