package com.example.tasky.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.tasky.database.converter.ListConverter
import com.example.tasky.database.dao.AgendaAlarmDao
import com.example.tasky.database.dao.EventDao
import com.example.tasky.database.dao.OfflineHistoryDao
import com.example.tasky.database.dao.ReminderDao
import com.example.tasky.database.dao.TaskDao
import com.example.tasky.database.model.AgendaAlarmEntity
import com.example.tasky.database.model.EventEntity
import com.example.tasky.database.model.OfflineHistoryEntity
import com.example.tasky.database.model.ReminderEntity
import com.example.tasky.database.model.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal lateinit var database: AppDatabase

@Database(
    entities = [ReminderEntity::class, TaskEntity::class, EventEntity::class, OfflineHistoryEntity::class, AgendaAlarmEntity::class],
    version = 1,
)
@TypeConverters(ListConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    abstract fun taskDao(): TaskDao

    abstract fun eventDao(): EventDao

    abstract fun offlineHistoryDao(): OfflineHistoryDao

    abstract fun agendaAlarmDao(): AgendaAlarmDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun createDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
        .also {
            database = it
        }

internal const val DB_FILE_NAME = "tasky.db"
