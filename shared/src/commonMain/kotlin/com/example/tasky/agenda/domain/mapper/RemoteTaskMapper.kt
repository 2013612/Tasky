package com.example.tasky.agenda.domain.mapper

import com.example.tasky.agenda.data.model.RemoteTask
import com.example.tasky.agenda.domain.model.Task
import kotlin.time.DurationUnit

fun Task.toRemoteTask(): RemoteTask =
    RemoteTask(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = time - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
        isDone = this.isDone,
    )
