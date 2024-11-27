package com.example.tasky.database.mock.dao

import com.example.tasky.database.dao.ReminderDao
import com.example.tasky.database.model.ReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReminderDaoMock(
    val entities: MutableList<ReminderEntity>,
) : ReminderDao {
    override suspend fun getById(id: String): ReminderEntity = entities.first { it.id == id }

    override suspend fun upsert(reminder: ReminderEntity) {
        entities += reminder
    }

    override suspend fun delete(reminder: ReminderEntity) {
        entities.remove(reminder)
    }

    override suspend fun deleteAll() {
        entities.clear()
    }

    override suspend fun getByTime(
        startTime: Long,
        endTime: Long,
    ): List<ReminderEntity> = entities.filter { it.time in startTime..<endTime }

    override fun getByTimeFlow(
        startTime: Long,
        endTime: Long,
    ): Flow<List<ReminderEntity>> =
        flow {
            entities.filter { it.time in startTime..<endTime }
        }
}
