package com.example.tasky.database.mock

import androidx.room.InvalidationTracker
import com.example.tasky.database.AppDatabase
import com.example.tasky.database.dao.AgendaAlarmDao
import com.example.tasky.database.dao.EventDao
import com.example.tasky.database.dao.OfflineHistoryDao
import com.example.tasky.database.dao.ReminderDao
import com.example.tasky.database.dao.TaskDao

class AppDatabaseMock(
    private val reminderDao: ReminderDao,
    private val taskDao: TaskDao,
    private val eventDao: EventDao,
    private val offlineHistoryDao: OfflineHistoryDao,
    private val agendaAlarmDao: AgendaAlarmDao,
) : AppDatabase() {
    private lateinit var unusedTracker: InvalidationTracker

    override fun createInvalidationTracker(): InvalidationTracker = unusedTracker

    override fun reminderDao(): ReminderDao = reminderDao

    override fun taskDao(): TaskDao = taskDao

    override fun eventDao(): EventDao = eventDao

    override fun offlineHistoryDao(): OfflineHistoryDao = offlineHistoryDao

    override fun agendaAlarmDao(): AgendaAlarmDao = agendaAlarmDao
}
