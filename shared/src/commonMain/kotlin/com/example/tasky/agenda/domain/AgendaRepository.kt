package com.example.tasky.agenda.domain

import com.example.tasky.agenda.data.AgendaDataSource
import com.example.tasky.agenda.data.model.CreateEventBody
import com.example.tasky.agenda.data.model.CreateReminderBody
import com.example.tasky.agenda.data.model.CreateTaskBody
import com.example.tasky.agenda.data.model.GetAgendaResponse
import com.example.tasky.agenda.data.model.UpdateEventBody
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.model.BaseError
import com.example.tasky.common.model.ResultWrapper
import com.example.tasky.common.model.map

interface IAgendaRepository {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError>

    suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError>

    suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError>

    suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError>

    suspend fun updateEvent(body: UpdateEventBody): ResultWrapper<Event, BaseError>

    suspend fun createTask(body: CreateTaskBody): ResultWrapper<Unit, BaseError>

    suspend fun createReminder(body: CreateReminderBody): ResultWrapper<Unit, BaseError>

    suspend fun createEvent(body: CreateEventBody): ResultWrapper<Event, BaseError>

    suspend fun getTask(taskId: String): ResultWrapper<Task, BaseError>

    suspend fun getEvent(eventId: String): ResultWrapper<Event, BaseError>

    suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, BaseError>
}

class AgendaRepository(
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) = agendaDataSource.getAgenda(timeStamp = timeStamp)

    override suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError> =
        when (agendaItem) {
            is Event -> agendaDataSource.deleteEvent(agendaItem.id)
            is Task -> agendaDataSource.deleteTask(agendaItem.id)
            is Reminder -> agendaDataSource.deleteReminder(agendaItem.id)
        }

    override suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError> = agendaDataSource.updateTask(task)

    override suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> = agendaDataSource.updateReminder(reminder)

    override suspend fun updateEvent(body: UpdateEventBody): ResultWrapper<Event, BaseError> =
        agendaDataSource.updateEvent(body = body).map {
            Event(it)
        }

    override suspend fun createTask(body: CreateTaskBody): ResultWrapper<Unit, BaseError> = agendaDataSource.createTask(body = body)

    override suspend fun createReminder(body: CreateReminderBody): ResultWrapper<Unit, BaseError> =
        agendaDataSource.createReminder(body = body)

    override suspend fun createEvent(body: CreateEventBody): ResultWrapper<Event, BaseError> =
        agendaDataSource.createEvent(body = body).map {
            Event(it)
        }

    override suspend fun getTask(taskId: String): ResultWrapper<Task, BaseError> = agendaDataSource.getTask(taskId = taskId)

    override suspend fun getEvent(eventId: String): ResultWrapper<Event, BaseError> =
        agendaDataSource.getEvent(eventId = eventId).map {
            Event(it)
        }

    override suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, BaseError> =
        agendaDataSource.getReminder(reminderId = reminderId)
}
