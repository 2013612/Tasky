package com.example.tasky.android.alarm.domain.mapper

import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.android.alarm.domain.model.NotificationData
import com.example.tasky.common.domain.util.toLocalDateTime

fun AgendaItem.toNotificationData() =
    NotificationData(
        requestCode = hashCode(),
        agendaId = id,
        title = title,
        description = description,
        type = getAgendaType(),
        startTime = getStartTime().toLocalDateTime(),
        remindAtType = remindAt,
    )
