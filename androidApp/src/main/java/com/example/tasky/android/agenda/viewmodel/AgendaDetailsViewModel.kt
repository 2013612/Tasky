package com.example.tasky.android.agenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.android.agenda.components.AgendaEditTextType
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenEvent
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenState
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenType
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import com.example.tasky.model.agenda.copy
import com.example.tasky.model.onSuccess
import com.example.tasky.repository.IAgendaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class AgendaDetailsViewModel(
    private val agendaId: String,
    private val agendaType: AgendaDetailsScreenType,
    private val agendaRepository: IAgendaRepository,
) : ViewModel() {
    private val _screenStateFlow =
        MutableStateFlow(
            AgendaDetailsScreenState(
                agendaItem =
                    when (agendaType) {
                        AgendaDetailsScreenType.TASK -> Task.EMPTY
                        AgendaDetailsScreenType.EVENT -> Event.EMPTY
                        AgendaDetailsScreenType.REMINDER -> Reminder.EMPTY
                    },
            ),
        )
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val _isDeleteSuccess = MutableStateFlow(false)
    val isDeleteSuccess = _isDeleteSuccess.asStateFlow()

    fun onEvent(event: AgendaDetailsScreenEvent) {
        when (event) {
            AgendaDetailsScreenEvent.CloseEditText -> _screenStateFlow.update { it.copy(agendaEditTextType = null) }
            AgendaDetailsScreenEvent.OnCloseClick -> {}
            AgendaDetailsScreenEvent.OnDeleteClick -> deleteAgendaItem()
            is AgendaDetailsScreenEvent.OnDescChange -> updateDesc(event.newDesc)
            AgendaDetailsScreenEvent.OnDescClick -> _screenStateFlow.update { it.copy(agendaEditTextType = AgendaEditTextType.DESCRIPTION) }
            AgendaDetailsScreenEvent.OnEditClick -> _screenStateFlow.update { it.copy(isEdit = true) }
            is AgendaDetailsScreenEvent.OnRemindAtChange -> updateRemindAt(event.newRemindAtTime)
            AgendaDetailsScreenEvent.OnSaveClick -> saveUpdate()
            is AgendaDetailsScreenEvent.OnStartDateChange -> updateStartDate(event.newDate)
            is AgendaDetailsScreenEvent.OnStartTimeChange -> updateStartTime(event.newHour, event.newMinute)
            is AgendaDetailsScreenEvent.OnTitleChange -> updateTitle(event.newTitle)
            AgendaDetailsScreenEvent.OnTitleClick -> _screenStateFlow.update { it.copy(agendaEditTextType = AgendaEditTextType.TITLE) }
        }
    }

    private fun deleteAgendaItem() {
        viewModelScope.launch {
            agendaRepository.deleteAgenda(screenStateFlow.value.agendaItem).onSuccess {
                _isDeleteSuccess.update { true }
            }
        }
    }

    private fun updateDesc(newDesc: String) {
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(description = newDesc))
        }
    }

    private fun updateTitle(newTitle: String) {
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(title = newTitle))
        }
    }

    private fun updateStartTime(
        hour: Int,
        minute: Int,
    ) {
        val agendaItem = screenStateFlow.value.agendaItem
        val timeMilli = hour.hours.toLong(DurationUnit.MILLISECONDS) + minute.minutes.toLong(DurationUnit.MILLISECONDS)
        val oneDayMilli = 1.days.toLong(DurationUnit.MILLISECONDS)
        val newDateTime = agendaItem.getStartTime() / oneDayMilli * oneDayMilli + timeMilli
        val remindAtDiff = agendaItem.getStartTime() - agendaItem.remindAt
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(startTime = newDateTime, remindAt = newDateTime - remindAtDiff))
        }
    }

    private fun updateStartDate(newDate: Long) {
        val agendaItem = screenStateFlow.value.agendaItem
        val oneDayMilli = 1.days.toLong(DurationUnit.MILLISECONDS)
        val newDateTime = newDate + agendaItem.getStartTime() % oneDayMilli
        val remindAtDiff = agendaItem.getStartTime() - agendaItem.remindAt
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(startTime = newDateTime, remindAt = newDateTime - remindAtDiff))
        }
    }

    private fun updateRemindAt(newRemindAt: Long) {
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(remindAt = it.agendaItem.getStartTime() - newRemindAt))
        }
    }

    private fun saveUpdate() {
        viewModelScope.launch {
            agendaRepository.updateAgenda(screenStateFlow.value.agendaItem).onSuccess {
                _screenStateFlow.update {
                    it.copy(isEdit = false)
                }
            }
        }
    }
}
