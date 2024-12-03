package com.example.tasky.android.agenda.presentation.agenda.model

import com.example.tasky.agenda.domain.model.AgendaItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AgendaScreenState(
    val agendas: ImmutableList<AgendaItemUi> = persistentListOf(),
    val name: String = "",
    val startDate: LocalDate =
        Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date,
    val numberOfDateShown: Int = 6,
    val selectedDateOffset: Int = 0,
    val showDeleteDialog: Boolean = false,
    val selectedAgendaItem: AgendaItem? = null,
)
