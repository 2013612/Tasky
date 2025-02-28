package com.example.tasky.android.agenda.presentation.agenda.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.RemindAtType
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.agenda.presentation.agenda.model.AgendaItemUi
import com.example.tasky.android.agenda.presentation.agenda.model.AgendaScreenEvent
import com.example.tasky.android.agenda.presentation.agenda.model.AgendaScreenState
import com.example.tasky.auth.domain.IAuthRepository
import com.example.tasky.auth.domain.ISessionManager
import com.example.tasky.auth.domain.util.getAvatarDisplayName
import com.example.tasky.common.domain.model.onSuccess
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlin.time.DurationUnit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface AgendaOneTimeEvent {
    data class OnAgendaCreate(
        val id: String,
        val type: AgendaType,
    ) : AgendaOneTimeEvent
}

class AgendaViewModel(
    private val agendaRepository: IAgendaRepository,
    private val authRepository: IAuthRepository,
    private val sessionManager: ISessionManager,
) : ViewModel() {
    companion object {
        private const val DEFAULT_DAYS_TO_SHOW = 6
    }

    private val _screenStateFlow = MutableStateFlow(AgendaScreenState(numberOfDateShown = DEFAULT_DAYS_TO_SHOW))
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val eventsChannel = Channel<AgendaOneTimeEvent>()
    val eventsFlow = eventsChannel.receiveAsFlow()

    private val selectedTimeFlow = MutableStateFlow(System.currentTimeMillis())

    init {
        subscribeSelectedTimeToGetAgenda()
        updateName()
    }

    fun onEvent(event: AgendaScreenEvent) {
        when (event) {
            AgendaScreenEvent.OnClickLogout -> logout()
            is AgendaScreenEvent.OnCreateClick -> createAgendaItem(event.type)
            is AgendaScreenEvent.OnDateSelect -> {
                _screenStateFlow.update {
                    it.copy(
                        startDate = event.newDate,
                        selectedDateOffset = 0,
                    )
                }
                selectedTimeFlow.update {
                    event.newDate
                        .atStartOfDayIn(
                            TimeZone.currentSystemDefault(),
                        ).toEpochMilliseconds()
                }
            }
            is AgendaScreenEvent.OnDayOffsetSelect -> {
                _screenStateFlow.update { it.copy(selectedDateOffset = event.newOffset) }
                val newDate =
                    _screenStateFlow.value.startDate
                        .plus(event.newOffset, DateTimeUnit.DAY)
                        .atStartOfDayIn(
                            TimeZone.currentSystemDefault(),
                        ).toEpochMilliseconds()
                selectedTimeFlow.update { newDate }
            }
            is AgendaScreenEvent.OnDeleteClick ->
                _screenStateFlow.update {
                    it.copy(
                        showDeleteDialog = true,
                        selectedAgendaItem = event.agendaItem,
                    )
                }
            is AgendaScreenEvent.OnEditClick -> {}
            is AgendaScreenEvent.OnOpenClick -> {}
            is AgendaScreenEvent.OnAgendaCircleClick -> toggleTaskIsDone(event.task)
            is AgendaScreenEvent.OnDeleteDialogClose -> closeDeleteAgenda(event.isCancel)
        }
    }

    private fun closeDeleteAgenda(isCancel: Boolean) {
        if (!isCancel) {
            val agendaItem = screenStateFlow.value.selectedAgendaItem ?: return
            deleteAgenda(agendaItem)
        }

        _screenStateFlow.update { it.copy(showDeleteDialog = false) }
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

    @ExperimentalCoroutinesApi
    private fun subscribeSelectedTimeToGetAgenda() {
        selectedTimeFlow
            .flatMapLatest {
                agendaRepository.getAgendaFlow(timeStamp = it)
            }.onEach { data ->
                val index = getTimeNeedleDisplayIndex(data)
                val itemUiList: MutableList<AgendaItemUi> = data.map { AgendaItemUi.Item(it) }.toMutableList()
                itemUiList.add(index, AgendaItemUi.Needle)

                _screenStateFlow.update { it.copy(agendas = itemUiList.toImmutableList()) }
            }.launchIn(viewModelScope)
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
            val fullName = sessionManager.getFullName() ?: return@launch
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
    private fun createAgendaItem(type: AgendaType) {
        viewModelScope.launch {
            val id = Uuid.random().toString()
            val now = Clock.System.now().toEpochMilliseconds()

            when (type) {
                AgendaType.TASK ->
                    agendaRepository.createTask(
                        Task.EMPTY.copy(id = id, time = now, remindAt = RemindAtType.TEN_MINUTE),
                    )
                AgendaType.EVENT -> {
                    val userId = sessionManager.getUserId() ?: ""
                    val attendee =
                        Attendee(
                            email = "",
                            fullName = sessionManager.getFullName() ?: "",
                            userId = sessionManager.getUserId() ?: "",
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
                            isUserEventCreator = true,
                        ),
                    )
                }
                AgendaType.REMINDER ->
                    agendaRepository.createReminder(
                        Reminder.EMPTY.copy(id = id, time = now, remindAt = RemindAtType.TEN_MINUTE),
                    )
            }.onSuccess {
                eventsChannel.send(AgendaOneTimeEvent.OnAgendaCreate(id, type))
            }
        }
    }
}
