package com.example.tasky.alarm.domain

import com.example.tasky.alarm.domain.model.AgendaAlarm

interface IAlarmRepository {
    suspend fun getAgendaAlarm(agendaId: String): AgendaAlarm?

    suspend fun getAllAgendaAlarm(): List<AgendaAlarm>

    suspend fun upsertAgendaAlarm(agendaAlarm: AgendaAlarm)

    suspend fun deleteAgendaAlarm(agendaId: String)

    suspend fun deleteAgendaAlarm(requestCode: Int)

    suspend fun deleteAllAgendaAlarm()
}
