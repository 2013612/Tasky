package com.example.tasky.agenda.data

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.database.AppDatabase
import com.example.tasky.database.database
import com.example.tasky.database.mapper.toEventEntity
import com.example.tasky.database.mapper.toReminderEntity
import com.example.tasky.database.mapper.toTaskEntity
import com.example.tasky.login.domain.manager.SessionManager

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

    suspend fun upsertEvent(
        event: Event,
        isGoing: Boolean,
    ) {
        val userId = SessionManager.getUserId() ?: ""
        val newAttendees = event.attendees.toMutableList()
        val attendeeIndex = newAttendees.indexOfFirst { it.userId == userId }
        if (attendeeIndex >= 0) {
            val newAttendee = newAttendees[attendeeIndex].copy(isGoing = isGoing)
            newAttendees[attendeeIndex] = newAttendee
        }

        appDatabase.eventDao().upsertEvent(
            event.copy(attendees = newAttendees).toEventEntity(),
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
}
