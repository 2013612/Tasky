package com.example.tasky.alarm.domain

import com.example.tasky.alarm.domain.model.AgendaAlarm

interface IAlarmRepository {
    suspend fun getAgendaAlarm(agendaId: String): AgendaAlarm

    suspend fun getAllAgendaAlarm(): List<AgendaAlarm>

    suspend fun createAgendaAlarm(agendaAlarm: AgendaAlarm)

    suspend fun deleteAgendaAlarm(agendaId: String)

    suspend fun deleteAllAgendaAlarm()
}
