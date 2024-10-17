package com.example.tasky.android.agenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.agenda.screen.AgendaScreenState
import com.example.tasky.model.agenda.AgendaItem
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
                val list = mutableListOf<AgendaItem>()
                list.addAll(data.events)
                list.addAll(data.tasks)
                list.addAll(data.reminders)
                list.sortWith(
                    java.util.Comparator { item1, item2 ->
                        val time1 =
                            when (item1) {
                                is Event -> item1.from
                                is Task -> item1.time
                                is Reminder -> item1.time
                            }
                        val time2 =
                            when (item2) {
                                is Event -> item2.from
                                is Task -> item2.time
                                is Reminder -> item2.time
                            }

                        return@Comparator time1.compareTo(time2)
                    },
                )
                _screenStateFlow.update { it.copy(agendas = list.toImmutableList()) }
            }
        }
    }
}
