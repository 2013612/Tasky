package com.example.tasky.database.mock.dao

import com.example.tasky.database.dao.OfflineHistoryDao
import com.example.tasky.database.model.OfflineHistoryEntity

class OfflineHistoryDaoMock(
    val entities: MutableList<OfflineHistoryEntity>,
) : OfflineHistoryDao {
    override suspend fun getAll(): List<OfflineHistoryEntity> = entities

    override suspend fun deleteAll() {
        entities.clear()
    }

    override suspend fun insert(entity: OfflineHistoryEntity) {
        entities += entity
    }

    override suspend fun delete(entity: OfflineHistoryEntity) {
        entities.remove(entity)
    }
}
