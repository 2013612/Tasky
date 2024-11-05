package com.example.tasky.agenda.domain

import com.example.tasky.agenda.data.AgendaDataSource
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.model.BaseError
import com.example.tasky.common.model.ResultWrapper
import com.example.tasky.common.model.map

interface IAgendaRepository {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<List<AgendaItem>, BaseError>

    suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError>

    suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError>

    suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError>

    suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError>

    suspend fun createTask(task: Task): ResultWrapper<Unit, BaseError>

    suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, BaseError>

    suspend fun createEvent(
        event: Event,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError>

    suspend fun getTask(taskId: String): ResultWrapper<Task, BaseError>

    suspend fun getEvent(eventId: String): ResultWrapper<Event, BaseError>

    suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, BaseError>
}

class AgendaRepository(
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) =
        agendaDataSource.getAgenda(timeStamp = timeStamp).map { response ->
            response.events.map { Event(it) } + response.tasks.map { Task(it) } + response.reminders.map { Reminder(it) }
        }

    override suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError> =
        when (agendaItem) {
            is Event -> agendaDataSource.deleteEvent(agendaItem.id)
            is Task -> agendaDataSource.deleteTask(agendaItem.id)
            is Reminder -> agendaDataSource.deleteReminder(agendaItem.id)
        }

    override suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError> = agendaDataSource.updateTask(task.toRemoteTask())

    override suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> =
        agendaDataSource.updateReminder(reminder.toRemoteReminder())

    override suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError> =
        agendaDataSource.updateEvent(body = event.toUpdateEventBody(deletedPhotoKeys, isGoing), photos = photos).map {
            Event(it)
        }

    override suspend fun createTask(task: Task): ResultWrapper<Unit, BaseError> = agendaDataSource.createTask(body = task.toRemoteTask())

    override suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> =
        agendaDataSource.createReminder(body = reminder.toRemoteReminder())

    override suspend fun createEvent(
        event: Event,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError> =
        agendaDataSource.createEvent(body = event.toCreateEventBody(photos)).map {
            Event(it)
        }

    override suspend fun getTask(taskId: String): ResultWrapper<Task, BaseError> =
        agendaDataSource.getTask(taskId = taskId).map {
            Task(it)
        }

    override suspend fun getEvent(eventId: String): ResultWrapper<Event, BaseError> =
        agendaDataSource.getEvent(eventId = eventId).map {
            Event(it)
        }

    override suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, BaseError> =
        agendaDataSource.getReminder(reminderId = reminderId).map {
            Reminder(it)
        }
}
