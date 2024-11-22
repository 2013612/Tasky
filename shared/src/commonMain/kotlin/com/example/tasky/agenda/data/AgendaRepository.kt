package com.example.tasky.agenda.data

import com.example.tasky.agenda.data.mapper.toEvent
import com.example.tasky.agenda.data.mapper.toReminder
import com.example.tasky.agenda.data.mapper.toTask
import com.example.tasky.agenda.data.model.CreateEventBody
import com.example.tasky.agenda.data.model.RemoteReminder
import com.example.tasky.agenda.data.model.RemoteTask
import com.example.tasky.agenda.data.model.UpdateEventBody
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.alarm.domain.IAlarmScheduler
import com.example.tasky.alarm.domain.mapper.toNotificationData
import com.example.tasky.auth.domain.manager.SessionManager
import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.common.domain.model.onSuccess
import com.example.tasky.database.model.ApiType
import com.example.tasky.database.model.isDelete
import dev.tmapps.konnection.Konnection
import kotlinx.serialization.json.Json

class AgendaRepository(
    private val alarmScheduler: IAlarmScheduler,
    private val alarmRepository: IAlarmRepository,
    private val agendaDataSource: AgendaDataSource = AgendaDataSource(),
    private val agendaLocalDataSource: AgendaLocalDataSource = AgendaLocalDataSource(),
    private val konnection: Konnection = Konnection.instance,
) : IAgendaRepository {
    override suspend fun getAgenda(timeStamp: Long) = ResultWrapper.Success(agendaLocalDataSource.getDayAgenda(timeStamp))

    override fun getAgendaFlow(timeStamp: Long) = agendaLocalDataSource.getDaySortedAgendaFlow(timeStamp)

    override suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, DataError.Remote> {
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
        }.onSuccess {
            alarmRepository.getAgendaAlarm(agendaItem.id)?.let {
                alarmScheduler.cancel(it.requestCode)
            }
        }
    }

    override suspend fun updateTask(task: Task): ResultWrapper<Unit, DataError.Remote> {
        agendaLocalDataSource.upsertTask(task)

        return if (konnection.isConnected()) {
            agendaDataSource.updateTask(task)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryUpdateTask(task, userId)

            return ResultWrapper.Success(Unit)
        }.onSuccess {
            val requestCode = alarmRepository.getAgendaAlarm(task.id)?.requestCode

            val notificationData = task.toNotificationData(requestCode)

            alarmScheduler.schedule(notificationData)
        }
    }

    override suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, DataError.Remote> {
        agendaLocalDataSource.upsertReminder(reminder)

        return if (konnection.isConnected()) {
            agendaDataSource.updateReminder(reminder)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryUpdateReminder(reminder, userId)
            ResultWrapper.Success(Unit)
        }.onSuccess {
            val requestCode = alarmRepository.getAgendaAlarm(reminder.id)?.requestCode

            val notificationData = reminder.toNotificationData(requestCode)

            alarmScheduler.schedule(notificationData)
        }
    }

    override suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, DataError.Remote> {
        agendaLocalDataSource.upsertEvent(event)

        return if (konnection.isConnected()) {
            val result =
                agendaDataSource.updateEvent(event, deletedPhotoKeys, isGoing, photos = photos).map {
                    it.toEvent()
                }

            result.onSuccess {
                agendaLocalDataSource.upsertEvent(it)
            }

            result
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryUpdateEvent(event, isGoing, userId)

            ResultWrapper.Success(event)
        }.onSuccess {
            val requestCode = alarmRepository.getAgendaAlarm(event.id)?.requestCode

            val notificationData = event.toNotificationData(requestCode)

            alarmScheduler.schedule(notificationData)
        }
    }

    override suspend fun createTask(task: Task): ResultWrapper<Unit, DataError.Remote> {
        agendaLocalDataSource.upsertTask(task)

        return if (konnection.isConnected()) {
            agendaDataSource.createTask(task)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryCreateTask(task, userId)
            ResultWrapper.Success(Unit)
        }.onSuccess {
            val notificationData = task.toNotificationData()
            alarmScheduler.schedule(notificationData)
        }
    }

    override suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, DataError.Remote> {
        agendaLocalDataSource.upsertReminder(reminder)

        return if (konnection.isConnected()) {
            agendaDataSource.createReminder(reminder)
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryCreateReminder(reminder, userId)
            ResultWrapper.Success(Unit)
        }.onSuccess {
            val notificationData = reminder.toNotificationData()
            alarmScheduler.schedule(notificationData)
        }
    }

    override suspend fun createEvent(event: Event): ResultWrapper<Event, DataError.Remote> {
        agendaLocalDataSource.upsertEvent(event = event)

        return if (konnection.isConnected()) {
            agendaDataSource.createEvent(event = event).map {
                it.toEvent()
            }
        } else {
            val userId = SessionManager.getUserId() ?: ""
            agendaLocalDataSource.insertOfflineHistoryCreateEvent(event, userId)
            return ResultWrapper.Success(event)
        }.onSuccess {
            val notificationData = event.toNotificationData()
            alarmScheduler.schedule(notificationData)
        }
    }

    override suspend fun getTask(taskId: String): ResultWrapper<Task, DataError.Remote> =
        ResultWrapper.Success(agendaLocalDataSource.getTask(taskId).toTask())

    override suspend fun getEvent(eventId: String): ResultWrapper<Event, DataError.Remote> =
        ResultWrapper.Success(agendaLocalDataSource.getEvent(eventId).toEvent())

    override suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, DataError.Remote> =
        ResultWrapper.Success(agendaLocalDataSource.getReminder(reminderId).toReminder())

    override suspend fun getAttendee(
        email: String,
        eventId: String,
        from: Long,
    ): ResultWrapper<Attendee?, DataError.Remote> =
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

    override suspend fun syncAgenda() {
        if (!konnection.isConnected()) {
            return
        }

        val histories = agendaLocalDataSource.getAllHistory()
        val userId = SessionManager.getUserId()
        val deletedEventIds = mutableListOf<String>()
        val deletedTaskIds = mutableListOf<String>()
        val deletedReminderIds = mutableListOf<String>()

        for (history in histories) {
            if (history.userId != userId) {
                agendaLocalDataSource.deleteHistory(history)
                continue
            }

            val json = Json

            val result: ResultWrapper<Any, DataError.Remote> =
                when (history.apiType) {
                    ApiType.DELETE_EVENT, ApiType.DELETE_EVENT_ATTENDEE -> {
                        deletedEventIds.add(history.params)
                        ResultWrapper.Success(Unit)
                    }
                    ApiType.DELETE_TASK -> {
                        deletedTaskIds.add(history.params)
                        ResultWrapper.Success(Unit)
                    }
                    ApiType.DELETE_REMINDER -> {
                        deletedReminderIds.add(history.params)
                        ResultWrapper.Success(Unit)
                    }
                    ApiType.CREATE_EVENT -> {
                        val body = json.decodeFromString<CreateEventBody>(history.body)
                        agendaDataSource.createEvent(body)
                    }
                    ApiType.CREATE_TASK -> {
                        val body = json.decodeFromString<RemoteTask>(history.body)
                        agendaDataSource.createTask(body)
                    }
                    ApiType.CREATE_REMINDER -> {
                        val body = json.decodeFromString<RemoteReminder>(history.body)
                        agendaDataSource.createReminder(body)
                    }
                    ApiType.UPDATE_EVENT -> {
                        val body = json.decodeFromString<UpdateEventBody>(history.body)
                        agendaDataSource.updateEvent(body = body, photos = emptyList())
                    }
                    ApiType.UPDATE_TASK -> {
                        val body = json.decodeFromString<RemoteTask>(history.body)
                        agendaDataSource.updateTask(body)
                    }
                    ApiType.UPDATE_REMINDER -> {
                        val body = json.decodeFromString<RemoteReminder>(history.body)
                        agendaDataSource.updateReminder(body)
                    }
                }

            if (!history.apiType.isDelete() && result is ResultWrapper.Success) {
                agendaLocalDataSource.deleteHistory(history)
            }
        }

        agendaDataSource.syncDeleteAgenda(deletedEventIds, deletedTaskIds, deletedReminderIds)
        agendaDataSource.getFullAgenda().onSuccess { response ->
            agendaLocalDataSource.clearAgendas()
            agendaLocalDataSource.upsertAgendas(
                response.events.map {
                    it.toEvent()
                },
                response.tasks.map { it.toTask() },
                response.reminders.map { it.toReminder() },
            )

            agendaLocalDataSource.deleteAllHistory()
        }
    }
}
