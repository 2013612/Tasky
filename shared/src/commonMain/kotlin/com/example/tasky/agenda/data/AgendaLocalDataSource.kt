package com.example.tasky.agenda.data

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

class AgendaLocalDataSource(
    private val appDatabase: AppDatabase = database,
) {
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
}
