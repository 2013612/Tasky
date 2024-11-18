package com.example.tasky.android.agenda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.RemindAtType
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenType
import com.example.tasky.android.agenda.screen.AgendaItemUi
import com.example.tasky.android.agenda.screen.AgendaScreenEvent
import com.example.tasky.android.agenda.screen.AgendaScreenState
import com.example.tasky.auth.domain.IAuthRepository
import com.example.tasky.auth.domain.manager.SessionManager
import com.example.tasky.auth.domain.util.getAvatarDisplayName
import com.example.tasky.common.domain.model.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface AgendaOneTimeEvent {
    data class OnAgendaCreate(
        val id: String,
        val type: AgendaDetailsScreenType,
    ) : AgendaOneTimeEvent
}

class AgendaViewModel(
    private val agendaRepository: IAgendaRepository,
    private val authRepository: IAuthRepository,
) : ViewModel() {
    companion object {
        private const val DEFAULT_DAYS_TO_SHOW = 6
    }

    private val _screenStateFlow = MutableStateFlow(AgendaScreenState(numberOfDateShown = DEFAULT_DAYS_TO_SHOW))
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val eventsChannel = Channel<AgendaOneTimeEvent>()
    val eventsFlow = eventsChannel.receiveAsFlow()

    private var getAgendasJob: Job? = null

    init {
        getAgendas(System.currentTimeMillis())
        updateName()
    }

    fun onEvent(event: AgendaScreenEvent) {
        when (event) {
            AgendaScreenEvent.OnClickLogout -> logout()
            is AgendaScreenEvent.OnCreateClick -> createAgendaItem(event.type)
            is AgendaScreenEvent.OnDateSelect -> {
                _screenStateFlow.update {
                    it.copy(
                        startDate = Instant.fromEpochMilliseconds(event.newDate),
                        selectedDateOffset = 0,
                    )
                }
                getAgendas(event.newDate)
            }
            is AgendaScreenEvent.OnDayOffsetSelect -> {
                _screenStateFlow.update { it.copy(selectedDateOffset = event.newOffset) }
                val newDate =
                    _screenStateFlow.value.startDate.toEpochMilliseconds() +
                        event.newOffset.toDuration(DurationUnit.DAYS).toLong(DurationUnit.MILLISECONDS)
                getAgendas(newDate)
            }
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
        getAgendasJob?.cancel()

        getAgendasJob =
            viewModelScope.launch {
                agendaRepository.getAgendaFlow(timeStamp = timeStamp).onSuccess { flow ->
                    flow.collectLatest { data ->
                        val itemList = data.sortedBy { it.getStartTime() }
                        val index = getTimeNeedleDisplayIndex(itemList)
                        val itemUiList: MutableList<AgendaItemUi> = itemList.map { AgendaItemUi.Item(it) }.toMutableList()
                        itemUiList.add(index, AgendaItemUi.Needle)

                        _screenStateFlow.update { it.copy(agendas = itemUiList.toImmutableList()) }
                    }
                }
            }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
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

    @OptIn(ExperimentalUuidApi::class)
    private fun createAgendaItem(type: AgendaDetailsScreenType) {
        viewModelScope.launch {
            val id = Uuid.random().toString()
            val now = Clock.System.now().toEpochMilliseconds()

            when (type) {
                AgendaDetailsScreenType.TASK ->
                    agendaRepository.createTask(
                        Task.EMPTY.copy(id = id, time = now, remindAt = RemindAtType.TEN_MINUTE),
                    )
                AgendaDetailsScreenType.EVENT -> {
                    val userId = SessionManager.getUserId() ?: ""
                    val attendee =
                        Attendee(
                            email = "",
                            fullName = SessionManager.getFullName() ?: "",
                            userId = SessionManager.getUserId() ?: "",
                            eventId = id,
                            isGoing = true,
                            remindAt =
                                now +
                                    RemindAtType.TEN_MINUTE.duration.toLong(
                                        DurationUnit.MILLISECONDS,
                                    ),
                        )

                    agendaRepository.createEvent(
                        Event.EMPTY.copy(
                            id = id,
                            from = now,
                            to = now,
                            remindAt = RemindAtType.TEN_MINUTE,
                            host = userId,
                            attendees = listOf(attendee),
                        ),
                    )
                }
                AgendaDetailsScreenType.REMINDER ->
                    agendaRepository.createReminder(
                        Reminder.EMPTY.copy(id = id, time = now, remindAt = RemindAtType.TEN_MINUTE),
                    )
            }.onSuccess {
                eventsChannel.send(AgendaOneTimeEvent.OnAgendaCreate(id, type))
            }
        }
    }
}
