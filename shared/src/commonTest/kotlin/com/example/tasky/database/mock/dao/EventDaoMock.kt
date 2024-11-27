package com.example.tasky.database.mock.dao

import com.example.tasky.database.dao.EventDao
import com.example.tasky.database.model.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventDaoMock(
    val entities: MutableList<EventEntity>,
) : EventDao {
    override suspend fun getById(id: String): EventEntity = entities.first { it.id == id }

    override suspend fun upsertEvent(event: EventEntity) {
        entities += event
    }

    override suspend fun delete(event: EventEntity) {
        entities.remove(event)
    }

    override suspend fun deleteAll() {
        entities.clear()
    }

    override suspend fun getByTime(
        startTime: Long,
        endTime: Long,
    ): List<EventEntity> = entities.filter { it.from in startTime..<endTime }

    override fun getByTimeFlow(
        startTime: Long,
        endTime: Long,
    ): Flow<List<EventEntity>> =
        flow {
            entities.filter { it.from in startTime..<endTime }
        }
}
