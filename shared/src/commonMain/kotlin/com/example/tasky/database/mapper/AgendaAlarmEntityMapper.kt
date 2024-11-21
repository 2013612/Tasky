package com.example.tasky.database.mapper

import com.example.tasky.alarm.domain.model.AgendaAlarm
import com.example.tasky.database.model.AgendaAlarmEntity

fun AgendaAlarm.toAgendaAlarmEntity() =
    AgendaAlarmEntity(
        agendaId,
        requestCode,
    )
