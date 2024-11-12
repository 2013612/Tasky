package com.example.tasky.agenda.domain

import com.example.tasky.agenda.data.AgendaDataSource
import com.example.tasky.agenda.data.AgendaLocalDataSource
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.common.domain.model.onSuccess
import com.example.tasky.login.domain.manager.SessionManager
import dev.tmapps.konnection.Konnection

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

    suspend fun getAttendee(
        email: String,
        eventId: String,
        from: Long,
    ): ResultWrapper<Attendee?, BaseError>
}

class AgendaRepository(
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
    private val agendaLocalDataSource: AgendaLocalDataSource = AgendaLocalDataSource(),
    private val konnection: Konnection = Konnection.instance,
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) =
        agendaDataSource.getAgenda(timeStamp = timeStamp).map { response ->
            response.events.map { Event(it) } + response.tasks.map { Task(it) } + response.reminders.map { Reminder(it) }
        }

    override suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, BaseError> {
        val userId = SessionManager.getUserId() ?: ""

        return when (agendaItem) {
            is Event -> {
                agendaLocalDataSource.deleteEvent(agendaItem.id)

                if (konnection.isConnected()) {
                    if (agendaItem.isUserEventCreator) {
                        agendaDataSource.deleteEvent(agendaItem.id)
                    } else {
                        agendaDataSource.deleteEventForAttendee(agendaItem.id)
                    }
                } else {
                    agendaLocalDataSource.insertOfflineHistoryDeleteEvent(agendaItem.id, isCreator = agendaItem.isUserEventCreator, userId)
                    ResultWrapper.Success(Unit)
                }
            }
            is Task -> {
                agendaLocalDataSource.deleteTask(agendaItem.id)

                if (konnection.isConnected()) {
                    agendaDataSource.deleteTask(agendaItem.id)
                } else {
                    agendaLocalDataSource.insertOfflineHistoryDeleteTask(agendaItem.id, userId)
                    ResultWrapper.Success(Unit)
                }
            }
            is Reminder -> {
                agendaLocalDataSource.deleteReminder(agendaItem.id)

                if (konnection.isConnected()) {
                    agendaDataSource.deleteReminder(agendaItem.id)
                } else {
                    agendaLocalDataSource.insertOfflineHistoryDeleteReminder(agendaItem.id, userId)
                    ResultWrapper.Success(Unit)
                }
            }
        }
    }

    override suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertTask(task)

        return if (konnection.isConnected()) {
            agendaDataSource.updateTask(task)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryUpdateTask(task, userId)

            return ResultWrapper.Success(Unit)
        }
    }

    override suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertReminder(reminder)

        return if (konnection.isConnected()) {
            agendaDataSource.updateReminder(reminder)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryUpdateReminder(reminder, userId)
            ResultWrapper.Success(Unit)
        }
    }

    override suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError> {
        agendaLocalDataSource.upsertEvent(event)

        if (konnection.isConnected()) {
            val result =
                agendaDataSource.updateEvent(event, deletedPhotoKeys, isGoing, photos = photos).map {
                    Event(it)
                }

            result.onSuccess {
                agendaLocalDataSource.upsertEvent(it)
            }

            return result
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryUpdateEvent(event, isGoing, userId)

            return ResultWrapper.Success(event)
        }
    }

    override suspend fun createTask(task: Task): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertTask(task)

        return if (konnection.isConnected()) {
            agendaDataSource.createTask(task)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryCreateTask(task, userId)
            ResultWrapper.Success(Unit)
        }
    }

    override suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> {
        agendaLocalDataSource.upsertReminder(reminder)

        return if (konnection.isConnected()) {
            agendaDataSource.createReminder(reminder)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryCreateReminder(reminder, userId)
            ResultWrapper.Success(Unit)
        }
    }

    override suspend fun createEvent(event: Event): ResultWrapper<Event, BaseError> {
        agendaLocalDataSource.upsertEvent(event = event)

        return if (konnection.isConnected()) {
            agendaDataSource.createEvent(event = event).map {
                Event(it)
            }
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryCreateEvent(event, userId)
            return ResultWrapper.Success(event)
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

    override suspend fun getAttendee(
        email: String,
        eventId: String,
        from: Long,
    ): ResultWrapper<Attendee?, BaseError> =
        agendaDataSource.getAttendee(email = email).map {
            if (it.doesUserExist) {
                Attendee(
                    email = it.attendee.email,
                    fullName = it.attendee.fullName,
                    userId = it.attendee.userId,
                    eventId = "",
                    isGoing = true,
                    remindAt = 0,
                )
            } else {
                null
            }
        }
}
