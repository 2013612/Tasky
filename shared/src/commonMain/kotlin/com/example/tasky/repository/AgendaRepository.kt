package com.example.tasky.repository

import com.example.tasky.dataSource.AgendaDataSource
import com.example.tasky.model.BaseError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.CreateReminderBody
import com.example.tasky.model.agenda.CreateTaskBody
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.GetAgendaResponse
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import com.example.tasky.model.agenda.UpdateEventBody

interface IAgendaRepository {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError>

    suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError>

    suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError>

    suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError>

    suspend fun updateEvent(body: UpdateEventBody): ResultWrapper<Event, BaseError>

    suspend fun createTask(body: CreateTaskBody): ResultWrapper<Unit, BaseError>

    suspend fun createReminder(body: CreateReminderBody): ResultWrapper<Unit, BaseError>
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

    override suspend fun updateEvent(body: UpdateEventBody): ResultWrapper<Event, BaseError> = agendaDataSource.updateEvent(body = body)

    override suspend fun createTask(body: CreateTaskBody): ResultWrapper<Unit, BaseError> = agendaDataSource.createTask(body = body)

    override suspend fun createReminder(body: CreateReminderBody): ResultWrapper<Unit, BaseError> =
        agendaDataSource.createReminder(body = body)
}
