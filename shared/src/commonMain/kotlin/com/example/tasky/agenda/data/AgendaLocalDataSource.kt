package com.example.tasky.agenda.data

import com.example.tasky.agenda.data.mapper.toCreateEventBody
import com.example.tasky.agenda.data.mapper.toRemoteReminder
import com.example.tasky.agenda.data.mapper.toRemoteTask
import com.example.tasky.agenda.data.mapper.toUpdateEventBody
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.database.AppDatabase
import com.example.tasky.database.database
import com.example.tasky.database.mapper.toEventEntity
import com.example.tasky.database.mapper.toReminderEntity
import com.example.tasky.database.mapper.toTaskEntity
import com.example.tasky.database.model.ApiType
import com.example.tasky.database.model.OfflineHistoryEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AgendaLocalDataSource(
    private val appDatabase: AppDatabase = database,
) {
    suspend fun getAllHistory(): List<OfflineHistoryEntity> = appDatabase.offlineHistoryDao().getAll()

    suspend fun deleteAllHistory() = appDatabase.offlineHistoryDao().deleteAll()

    fun deleteHistory(historyEntity: OfflineHistoryEntity) =
        appDatabase.offlineHistoryDao().delete(
            historyEntity,
        )

    fun deleteEvent(eventId: String) {
        appDatabase.eventDao().delete(Event.EMPTY.copy(id = eventId).toEventEntity())
    }

    fun deleteTask(taskId: String) {
        appDatabase.taskDao().delete(Task.EMPTY.copy(id = taskId).toTaskEntity())
    }

    fun deleteReminder(reminderId: String) {
        appDatabase.reminderDao().delete(Reminder.EMPTY.copy(id = reminderId).toReminderEntity())
    }

    fun upsertEvent(event: Event) {
        appDatabase.eventDao().upsertEvent(
            event.toEventEntity(),
        )
    }

    fun upsertTask(task: Task) {
        appDatabase.taskDao().upsert(task.toTaskEntity())
    }

    fun upsertReminder(reminder: Reminder) {
        appDatabase.reminderDao().upsert(
            reminder.toReminderEntity(),
        )
    }

    fun insertOfflineHistoryDeleteEvent(
        id: String,
        isCreator: Boolean,
        userId: String,
    ) {
        val entity =
            OfflineHistoryEntity(
                apiType = if (isCreator) ApiType.DELETE_EVENT else ApiType.DELETE_EVENT_ATTENDEE,
                params = id,
                body = "",
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryDeleteTask(
        id: String,
        userId: String,
    ) {
        val entity = OfflineHistoryEntity(apiType = ApiType.DELETE_TASK, params = id, body = "", userId = userId)
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryDeleteReminder(
        id: String,
        userId: String,
    ) {
        val entity = OfflineHistoryEntity(apiType = ApiType.DELETE_REMINDER, params = id, body = "", userId = userId)
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryCreateEvent(
        event: Event,
        userId: String,
    ) {
        val createEventJson = Json.encodeToString(event.toCreateEventBody())
        val entity =
            OfflineHistoryEntity(
                apiType = ApiType.CREATE_EVENT,
                params = "",
                body = createEventJson,
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryCreateTask(
        task: Task,
        userId: String,
    ) {
        val taskJson = Json.encodeToString(task.toRemoteTask())
        val entity =
            OfflineHistoryEntity(
                apiType = ApiType.CREATE_TASK,
                params = "",
                body = taskJson,
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryCreateReminder(
        reminder: Reminder,
        userId: String,
    ) {
        val reminderJson = Json.encodeToString(reminder.toRemoteReminder())
        val entity =
            OfflineHistoryEntity(
                apiType = ApiType.CREATE_REMINDER,
                params = "",
                body = reminderJson,
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryUpdateEvent(
        event: Event,
        isGoing: Boolean,
        userId: String,
    ) {
        val updateEventJson = Json.encodeToString(event.toUpdateEventBody(emptyList(), isGoing))
        val entity =
            OfflineHistoryEntity(
                apiType = ApiType.UPDATE_EVENT,
                params = "",
                body = updateEventJson,
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryUpdateTask(
        task: Task,
        userId: String,
    ) {
        val taskJson = Json.encodeToString(task.toRemoteTask())
        val entity =
            OfflineHistoryEntity(
                apiType = ApiType.UPDATE_TASK,
                params = "",
                body = taskJson,
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun insertOfflineHistoryUpdateReminder(
        reminder: Reminder,
        userId: String,
    ) {
        val reminderJson = Json.encodeToString(reminder.toRemoteReminder())
        val entity =
            OfflineHistoryEntity(
                apiType = ApiType.UPDATE_REMINDER,
                params = "",
                body = reminderJson,
                userId = userId,
            )
        appDatabase.offlineHistoryDao().insert(entity)
    }

    fun clearAgendas() {
        appDatabase.eventDao().deleteAll()
        appDatabase.taskDao().deleteAll()
        appDatabase.reminderDao().deleteAll()
    }

    fun upsertAgendas(
        events: List<Event>,
        tasks: List<Task>,
        reminders: List<Reminder>,
    ) {
        for (event in events) {
            upsertEvent(event)
        }
        for (task in tasks) {
            upsertTask(task)
        }
        for (reminder in reminders) {
            upsertReminder(reminder)
        }
    }
}
