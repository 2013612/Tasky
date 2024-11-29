package com.example.tasky.android.agenda.presentation.agenda.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class AgendaScreenState(
    val agendas: ImmutableList<AgendaItemUi> = persistentListOf(),
    val name: String = "",
    val startDate: Instant = Clock.System.now(),
    val numberOfDateShown: Int = 6,
    val selectedDateOffset: Int = 0,
)
