package com.example.tasky.android.agenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.agenda.screen.AgendaScreenState
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import com.example.tasky.model.onSuccess
import com.example.tasky.repository.IAgendaRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgendaViewModel(
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(AgendaScreenState())
    val screenStateFlow = _screenStateFlow.asStateFlow()

    fun getAgendas(timeStamp: Long) {
        viewModelScope.launch {
            agendaRepository.getAgenda(timeStamp = timeStamp).onSuccess { data ->
                val list =
                    (data.events + data.tasks + data.reminders)
                        .sortedBy {
                            when (it) {
                                is Event -> it.from
                                is Task -> it.time
                                is Reminder -> it.time
                            }
                        }
                _screenStateFlow.update { it.copy(agendas = list.toImmutableList()) }
            }
        }
    }
}
