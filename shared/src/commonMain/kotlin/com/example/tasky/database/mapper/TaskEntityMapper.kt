package com.example.tasky.database.mapper

import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.database.model.TaskEntity

fun Task.toTaskEntity(): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        description = description,
        time = time,
        remindAt = remindAt,
        isDone = isDone,
    )
