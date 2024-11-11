package com.example.tasky.agenda.domain

import com.example.tasky.agenda.data.AgendaDataSource
import com.example.tasky.agenda.data.AgendaLocalDataSource
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.common.domain.model.onSuccess

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

    suspend fun createEvent(event: Event): ResultWrapper<Event, BaseError>

    suspend fun getTask(taskId: String): ResultWrapper<Task, BaseError>

    suspend fun getEvent(eventId: String): ResultWrapper<Event, BaseError>

    suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, BaseError>
}

class AgendaRepository(
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
    private val agendaLocalDataSource: AgendaLocalDataSource = AgendaLocalDataSource(),
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) =
        agendaDataSource.getAgenda(timeStamp = timeStamp).map { response ->
            response.events.map { Event(it) } + response.tasks.map { Task(it) } + response.reminders.map { Reminder(it) }
        }

    override suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError> =
        when (agendaItem) {
            is Event -> {
                agendaLocalDataSource.deleteEvent(agendaItem.id)
                agendaDataSource.deleteEvent(agendaItem.id)
            }
            is Task -> {
                agendaLocalDataSource.deleteTask(agendaItem.id)
                agendaDataSource.deleteTask(agendaItem.id)
            }
            is Reminder -> {
                agendaLocalDataSource.deleteReminder(agendaItem.id)
                agendaDataSource.deleteReminder(agendaItem.id)
            }
        }

    override suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertTask(task)

        return agendaDataSource.updateTask(task)
    }

    override suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertReminder(reminder)

        return agendaDataSource.updateReminder(reminder)
    }

    override suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError> {
        agendaLocalDataSource.upsertEvent(event)

        val result =
            agendaDataSource.updateEvent(event, deletedPhotoKeys, isGoing, photos = photos).map {
                Event(it)
            }

        result.onSuccess {
            agendaLocalDataSource.upsertEvent(it)
        }

        return result
    }

    override suspend fun createTask(task: Task): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertTask(task)

        return agendaDataSource.createTask(task)
    }

    override suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertReminder(reminder)

        return agendaDataSource.createReminder(reminder)
    }

    override suspend fun createEvent(event: Event): ResultWrapper<Event, BaseError> {
        agendaLocalDataSource.upsertEvent(event = event)

        return agendaDataSource.createEvent(event = event).map {
            Event(it)
        }
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
