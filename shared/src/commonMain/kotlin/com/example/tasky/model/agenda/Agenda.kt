package com.example.tasky.model.agenda

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
