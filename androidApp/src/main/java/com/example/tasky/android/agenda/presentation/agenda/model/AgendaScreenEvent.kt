package com.example.tasky.android.agenda.presentation.agenda.model

import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.agenda.domain.model.Task
import kotlinx.datetime.LocalDate

sealed interface AgendaScreenEvent {
    data object OnClickLogout : AgendaScreenEvent

    data class OnCreateClick(
        val type: AgendaType,
    ) : AgendaScreenEvent

    data class OnDateSelect(
        val newDate: LocalDate,
    ) : AgendaScreenEvent

    data class OnDayOffsetSelect(
        val newOffset: Int,
    ) : AgendaScreenEvent

    data class OnOpenClick(
        val agendaItem: AgendaItem,
    ) : AgendaScreenEvent

    data class OnEditClick(
        val agendaItem: AgendaItem,
    ) : AgendaScreenEvent

    data class OnDeleteClick(
        val agendaItem: AgendaItem,
    ) : AgendaScreenEvent

    data class OnAgendaCircleClick(
        val task: Task,
    ) : AgendaScreenEvent

    data class OnDeleteDialogClose(
        val isCancel: Boolean,
    ) : AgendaScreenEvent
}
