package com.example.tasky.database.mock.dao

import com.example.tasky.database.dao.TaskDao
import com.example.tasky.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskDaoMock(
    val entities: MutableList<TaskEntity>,
) : TaskDao {
    override suspend fun getById(id: String): TaskEntity = entities.first { it.id == id }

    override suspend fun upsert(task: TaskEntity) {
        entities += task
    }

    override suspend fun delete(task: TaskEntity) {
        entities.remove(task)
    }

    override suspend fun deleteAll() {
        entities.clear()
    }

    override suspend fun getByTime(
        startTime: Long,
        endTime: Long,
    ): List<TaskEntity> = entities.filter { it.time in startTime..<endTime }

    override fun getByTimeFlow(
        startTime: Long,
        endTime: Long,
    ): Flow<List<TaskEntity>> =
        flow {
            entities.filter { it.time in startTime..<endTime }
        }
}
