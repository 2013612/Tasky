package com.example.tasky.database.mock.dao

import com.example.tasky.database.dao.AgendaAlarmDao
import com.example.tasky.database.model.AgendaAlarmEntity

class AgendaAlarmDaoMock(
    val entities: MutableList<AgendaAlarmEntity>,
) : AgendaAlarmDao {
    override suspend fun get(agendaId: String): AgendaAlarmEntity? = entities.first { it.agendaId == agendaId }

    override suspend fun getAll(): List<AgendaAlarmEntity> = entities

    override suspend fun deleteAll() {
        entities.clear()
    }

    override suspend fun deleteByRequestCode(requestCode: Int) {
        entities.removeAll { it.requestCode == requestCode }
    }

    override suspend fun upsert(entity: AgendaAlarmEntity) {
        entities += entity
    }

    override suspend fun delete(entity: AgendaAlarmEntity) {
        entities.remove(entity)
    }
}
