package com.example.tasky.agenda.data.model

import com.example.tasky.agenda.domain.model.Task
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/agenda")
class Agenda

@Serializable
data class GetAgendaResponse(
    val events: List<RemoteEvent>,
    val tasks: List<Task>,
    val reminders: List<RemoteReminder>,
)
