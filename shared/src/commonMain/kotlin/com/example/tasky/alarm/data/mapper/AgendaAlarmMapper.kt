package com.example.tasky.alarm.data.mapper

import com.example.tasky.alarm.domain.model.AgendaAlarm
import com.example.tasky.database.model.AgendaAlarmEntity

fun AgendaAlarmEntity.toAgendaAlarm() = AgendaAlarm(agendaId, requestCode)
