package com.example.tasky.agenda.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GetAgendaResponse(
    val events: List<RemoteEvent>,
    val tasks: List<RemoteTask>,
    val reminders: List<RemoteReminder>,
)
