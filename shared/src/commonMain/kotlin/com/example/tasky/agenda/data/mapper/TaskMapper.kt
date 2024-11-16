package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.RemoteTask
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.model.getRemindAtType
import com.example.tasky.database.model.TaskEntity

fun RemoteTask.toTask(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        time = time,
        remindAt = getRemindAtType(time, remindAt),
        isDone = isDone,
    )

fun TaskEntity.toTask(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        time = time,
        remindAt = remindAt,
        isDone = isDone,
    )
