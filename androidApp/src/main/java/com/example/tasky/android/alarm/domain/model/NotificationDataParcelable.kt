package com.example.tasky.android.alarm.domain.model

import android.os.Parcelable
import com.example.tasky.agenda.domain.model.AgendaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationDataParcelable(
    val agendaId: String,
    val title: String,
    val description: String?,
    val type: AgendaType,
    val startTime: Long,
) : Parcelable
