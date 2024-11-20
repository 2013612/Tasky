package com.example.tasky.android.alarm.domain

import com.example.tasky.agenda.domain.model.AgendaItem

interface IAlarmScheduler {
    fun schedule(agendaItem: AgendaItem)

    fun cancel(agendaItem: AgendaItem)
}
