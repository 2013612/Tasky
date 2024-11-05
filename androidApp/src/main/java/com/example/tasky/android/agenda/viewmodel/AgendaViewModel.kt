package com.example.tasky.android.agenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.agenda.screen.AgendaItemUi
import com.example.tasky.android.agenda.screen.AgendaScreenEvent
import com.example.tasky.android.agenda.screen.AgendaScreenState
import com.example.tasky.common.model.onSuccess
import com.example.tasky.login.domain.manager.SessionManager
import com.example.tasky.login.domain.manager.loginManager
import com.example.tasky.login.domain.util.getAvatarDisplayName
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class AgendaViewModel(
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    companion object {
        private const val DEFAULT_DAYS_TO_SHOW = 6
    }

    private val _screenStateFlow = MutableStateFlow(AgendaScreenState(numberOfDateShown = DEFAULT_DAYS_TO_SHOW))
    val screenStateFlow = _screenStateFlow.asStateFlow()

    fun initState() {
        getAgendas(System.currentTimeMillis())
        updateName()
    }

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
            is AgendaScreenEvent.OnAgendaCircleClick -> toggleTaskIsDone(event.task)
        }
    }

    private fun getTimeNeedleDisplayIndex(agendaItemsSortedByStartTime: List<AgendaItem>): Int {
        val currentTime = System.currentTimeMillis()
        val index = agendaItemsSortedByStartTime.indexOfFirst { it.getStartTime() > currentTime }

        return if (index < 0) {
            agendaItemsSortedByStartTime.lastIndex + 1
        } else {
            index
        }
    }

    private fun getAgendas(timeStamp: Long) {
        viewModelScope.launch {
            agendaRepository.getAgenda(timeStamp = timeStamp).onSuccess { data ->
                val itemList =
                    (data.events + data.tasks + data.reminders)
                        .sortedBy { it.getStartTime() }
                val index = getTimeNeedleDisplayIndex(itemList)
                val itemUiList: MutableList<AgendaItemUi> = itemList.map { AgendaItemUi.Item(it) }.toMutableList()
                itemUiList.add(index, AgendaItemUi.Needle)

                _screenStateFlow.update { it.copy(agendas = itemUiList.toImmutableList()) }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            loginManager.logOut()
        }
    }

    private fun deleteAgenda(agendaItem: AgendaItem) {
        viewModelScope.launch {
            agendaRepository.deleteAgenda(agendaItem).onSuccess {
                val newList = screenStateFlow.value.agendas.toMutableList()
                newList.remove(AgendaItemUi.Item(agendaItem))
                _screenStateFlow.update { it.copy(agendas = newList.toImmutableList()) }
            }
        }
    }

    private fun updateName() {
        viewModelScope.launch {
            val fullName = SessionManager.getFullName() ?: return@launch
            _screenStateFlow.update { it.copy(name = fullName.getAvatarDisplayName()) }
        }
    }

    private fun toggleTaskIsDone(task: Task) {
        viewModelScope.launch {
            val body = task.copy(isDone = task.isDone.not())
            agendaRepository.updateTask(body).onSuccess {
                val newList = screenStateFlow.value.agendas.toMutableList()
                val index = newList.indexOf(AgendaItemUi.Item(task))
                newList[index] = AgendaItemUi.Item(body)
                _screenStateFlow.update { it.copy(agendas = newList.toImmutableList()) }
            }
        }
    }
}
