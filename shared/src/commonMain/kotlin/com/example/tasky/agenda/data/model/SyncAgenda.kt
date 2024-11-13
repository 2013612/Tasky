package com.example.tasky.agenda.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncAgendaBody(
    val deletedEventIds: List<String>,
    val deletedTaskIds: List<String>,
    val deletedReminderIds: List<String>,
)
