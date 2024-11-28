package com.example.tasky.database.mock

import androidx.room.InvalidationTracker
import com.example.tasky.database.AppDatabase
import com.example.tasky.database.dao.AgendaAlarmDao
import com.example.tasky.database.dao.EventDao
import com.example.tasky.database.dao.OfflineHistoryDao
import com.example.tasky.database.dao.ReminderDao
import com.example.tasky.database.dao.TaskDao
import com.example.tasky.database.mock.dao.AgendaAlarmDaoMock
import com.example.tasky.database.mock.dao.EventDaoMock
import com.example.tasky.database.mock.dao.OfflineHistoryDaoMock
import com.example.tasky.database.mock.dao.ReminderDaoMock
import com.example.tasky.database.mock.dao.TaskDaoMock

class AppDatabaseMock(
    val reminderDao: ReminderDaoMock,
    val taskDao: TaskDaoMock,
    val eventDao: EventDaoMock,
    val offlineHistoryDao: OfflineHistoryDaoMock,
    val agendaAlarmDao: AgendaAlarmDaoMock,
) : AppDatabase() {
    private lateinit var unusedTracker: InvalidationTracker

    override fun createInvalidationTracker(): InvalidationTracker = unusedTracker

    override fun reminderDao(): ReminderDao = reminderDao

    override fun taskDao(): TaskDao = taskDao

    override fun eventDao(): EventDao = eventDao

    override fun offlineHistoryDao(): OfflineHistoryDao = offlineHistoryDao

    override fun agendaAlarmDao(): AgendaAlarmDao = agendaAlarmDao

    override fun clearAllTables() {
    }
}
