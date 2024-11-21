package com.example.tasky.android.alarm.domain.mapper

import com.example.tasky.android.alarm.domain.model.NotificationData
import com.example.tasky.android.alarm.domain.model.NotificationDataParcelable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun NotificationData.toNotificationDataParcelable() =
    NotificationDataParcelable(
        agendaId = agendaId,
        title = title,
        description = description,
        type = type,
        startTime = startTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
    )
