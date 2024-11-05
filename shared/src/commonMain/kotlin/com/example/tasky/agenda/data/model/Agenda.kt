package com.example.tasky.agenda.data.model

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/agenda")
class Agenda

@Serializable
data class GetAgendaResponse(
    val events: List<RemoteEvent>,
    val tasks: List<RemoteTask>,
    val reminders: List<RemoteReminder>,
)
