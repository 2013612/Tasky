package com.example.tasky.agenda.data.model

import kotlinx.serialization.Serializable

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
