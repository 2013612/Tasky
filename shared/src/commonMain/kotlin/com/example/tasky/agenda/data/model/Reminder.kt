package com.example.tasky.agenda.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteReminder(
    val id: String,
    val title: String,
    val description: String,
    val time: Long,
    val remindAt: Long,
)

typealias UpdateReminderBody = RemoteReminder

typealias CreateReminderBody = RemoteReminder
