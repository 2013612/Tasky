package com.example.tasky.alarm.data

import com.example.tasky.alarm.data.mapper.toAgendaAlarm
import com.example.tasky.alarm.domain.IAlarmRepository
import com.example.tasky.alarm.domain.model.AgendaAlarm
import com.example.tasky.database.mapper.toAgendaAlarmEntity

class AlarmRepository(
    private val localDataSource: AlarmLocalDataSource = AlarmLocalDataSource(),
) : IAlarmRepository {
    override suspend fun getAgendaAlarm(agendaId: String): AgendaAlarm = localDataSource.getAgendaAlarm(agendaId).toAgendaAlarm()

    override suspend fun getAllAgendaAlarm(): List<AgendaAlarm> = localDataSource.getAllAgendaAlarm().map { it.toAgendaAlarm() }

    override suspend fun createAgendaAlarm(agendaAlarm: AgendaAlarm) = localDataSource.createAgendaAlarm(agendaAlarm.toAgendaAlarmEntity())

    override suspend fun deleteAgendaAlarm(agendaId: String) = localDataSource.deleteAgendaAlarm(agendaId)

    override suspend fun deleteAgendaAlarm(requestCode: Int) = localDataSource.deleteAgendaAlarm(requestCode)

    override suspend fun deleteAllAgendaAlarm() = localDataSource.deleteAllAgendaAlarm()
}
