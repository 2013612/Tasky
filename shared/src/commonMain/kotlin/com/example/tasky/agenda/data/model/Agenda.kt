package com.example.tasky.agenda.data.model

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/agenda")
class Agenda

@Serializable
data class GetAgendaResponse(
    val events: List<Event>,
    val tasks: List<Task>,
    val reminders: List<Reminder>,
)
