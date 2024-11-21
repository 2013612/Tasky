package com.example.tasky.alarm.data

import com.example.tasky.database.AppDatabase
import com.example.tasky.database.database
import com.example.tasky.database.model.AgendaAlarmEntity

class AlarmLocalDataSource(
    private val appDatabase: AppDatabase = database,
) {
    suspend fun getAgendaAlarm(agendaId: String) = appDatabase.agendaAlarmDao().get(agendaId)

    suspend fun getAllAgendaAlarm() = appDatabase.agendaAlarmDao().getAll()

    suspend fun createAgendaAlarm(entity: AgendaAlarmEntity) = appDatabase.agendaAlarmDao().insert(entity)

    suspend fun deleteAgendaAlarm(agendaId: String) =
        appDatabase.agendaAlarmDao().delete(
            AgendaAlarmEntity(agendaId, 0),
        )

    suspend fun deleteAllAgendaAlarm() = appDatabase.agendaAlarmDao().deleteAll()
}
