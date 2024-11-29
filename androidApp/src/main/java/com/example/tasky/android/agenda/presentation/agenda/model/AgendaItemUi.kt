package com.example.tasky.android.agenda.presentation.agenda.model

import com.example.tasky.agenda.domain.model.AgendaItem

sealed interface AgendaItemUi {
    data class Item(
        val item: AgendaItem,
    ) : AgendaItemUi

    data object Needle : AgendaItemUi
}
