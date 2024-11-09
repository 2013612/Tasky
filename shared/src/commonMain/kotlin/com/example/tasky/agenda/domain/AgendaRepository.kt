package com.example.tasky.agenda.domain

import com.example.tasky.agenda.data.AgendaDataSource
import com.example.tasky.agenda.data.mapper.toCreateEventBody
import com.example.tasky.agenda.data.mapper.toRemoteReminder
import com.example.tasky.agenda.data.mapper.toRemoteTask
import com.example.tasky.agenda.data.mapper.toUpdateEventBody
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.RemindAtType
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.database.database
import com.example.tasky.database.mapper.toEventEntity
import com.example.tasky.database.mapper.toReminderEntity
import com.example.tasky.login.domain.manager.SessionManager
import kotlinx.datetime.Clock
import kotlin.time.DurationUnit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    suspend fun createLocalEvent(): Event

    suspend fun createLocalReminder(): Reminder

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

    override suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> {
        database.reminderDao().upsert(
            reminder.toReminderEntity(),
        )

        return agendaDataSource.createReminder(body = reminder.toRemoteReminder())
    }

    override suspend fun createEvent(
        event: Event,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, BaseError> {
        database.eventDao().upsertEvent(
            event.toEventEntity(),
        )

        return agendaDataSource.createEvent(body = event.toCreateEventBody(), photos = photos).map {
            Event(it)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createLocalEvent(): Event {
        val id = Uuid.random().toString()
        val now = Clock.System.now().toEpochMilliseconds()
        val userId = SessionManager.getUserId() ?: ""
        val attendee =
            Attendee(
                email = "",
                fullName = SessionManager.getFullName() ?: "",
                userId = SessionManager.getUserId() ?: "",
                eventId = id,
                isGoing = true,
                remindAt = now + RemindAtType.TEN_MINUTE.duration.toLong(DurationUnit.MILLISECONDS),
            )

        val event =
            Event.EMPTY
                .copy(
                    id = id,
                    from = now,
                    to = now,
                    host = userId,
                    attendees = listOf(attendee),
                )

        database.eventDao().upsertEvent(
            event.toEventEntity(),
        )

        return event
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createLocalReminder(): Reminder {
        val id = Uuid.random().toString()
        val now = Clock.System.now().toEpochMilliseconds()
        val reminder = Reminder.EMPTY.copy(id = id, time = now)

        database.reminderDao().upsert(reminder.toReminderEntity())

        return reminder
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
