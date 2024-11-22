package com.example.tasky.alarm.domain.model

import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.agenda.domain.model.RemindAtType
import kotlinx.datetime.LocalDateTime

data class NotificationData(
    val requestCode: Int,
    val agendaId: String,
    val title: String,
    val description: String?,
    val type: AgendaType,
    val startTime: LocalDateTime,
    val remindAtType: RemindAtType,
)
