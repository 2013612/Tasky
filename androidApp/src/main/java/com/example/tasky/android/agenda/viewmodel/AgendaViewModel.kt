package com.example.tasky.android.agenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.agenda.screen.AgendaScreenEvent
import com.example.tasky.android.agenda.screen.AgendaScreenState
import com.example.tasky.manager.loginManager
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.onSuccess
import com.example.tasky.repository.IAgendaRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class AgendaViewModel(
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(AgendaScreenState())
    val screenStateFlow = _screenStateFlow.asStateFlow()

    fun onEvent(event: AgendaScreenEvent) {
        when (event) {
            AgendaScreenEvent.OnClickLogout -> logout()
            AgendaScreenEvent.OnClickToCreateEvent -> {}
            AgendaScreenEvent.OnClickToCreateReminder -> {}
            AgendaScreenEvent.OnClickToCreateTask -> {}
            is AgendaScreenEvent.OnDateSelect ->
                _screenStateFlow.update {
                    it.copy(
                        startDate = Instant.fromEpochMilliseconds(event.newDate),
                    )
                }
            is AgendaScreenEvent.OnDayOffsetSelect -> _screenStateFlow.update { it.copy(selectedDateOffset = event.newOffset) }
            is AgendaScreenEvent.OnDeleteClick -> deleteAgenda(agendaItem = event.agendaItem)
            is AgendaScreenEvent.OnEditClick -> {}
            is AgendaScreenEvent.OnOpenClick -> {}
        }
    }

    fun getAgendas(timeStamp: Long) {
        viewModelScope.launch {
            agendaRepository.getAgenda(timeStamp = timeStamp).onSuccess { data ->
                val list =
                    (data.events + data.tasks + data.reminders)
                        .sortedBy { it.getStartTime() }
                _screenStateFlow.update { it.copy(agendas = list.toImmutableList()) }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            loginManager.logOut()
        }
    }

    private fun deleteAgenda(agendaItem: AgendaItem) {
        // TODO
    }
}
