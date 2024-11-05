package com.example.tasky.agenda.data.model

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/task")
class TaskPath

@Serializable
data class RemoteTask(
    val id: String,
    val title: String,
    val description: String,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean,
)

typealias UpdateTaskBody = RemoteTask

typealias CreateTaskBody = RemoteTask
