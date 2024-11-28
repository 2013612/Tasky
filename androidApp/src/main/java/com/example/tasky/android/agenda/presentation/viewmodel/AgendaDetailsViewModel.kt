package com.example.tasky.android.agenda.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tasky.agenda.domain.IAgendaRepository
import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.RemindAtType
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.model.copy
import com.example.tasky.android.agenda.domain.IImageCompressor
import com.example.tasky.android.agenda.presentation.components.details.DetailsEditTextType
import com.example.tasky.android.agenda.presentation.components.details.DetailsPhoto
import com.example.tasky.android.agenda.presentation.screen.AgendaDetails
import com.example.tasky.android.agenda.presentation.screen.AgendaDetailsScreenEvent
import com.example.tasky.android.agenda.presentation.screen.AgendaDetailsScreenState
import com.example.tasky.auth.domain.ISessionManager
import com.example.tasky.common.domain.INetworkManager
import com.example.tasky.common.domain.model.onSuccess
import com.example.tasky.common.domain.util.toLocalDateTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface AgendaDetailsOneTimeEvent {
    data object OnDeleteSuccess : AgendaDetailsOneTimeEvent

    data class OnPhotoSkipped(
        val skippedPhotoCount: Int,
    ) : AgendaDetailsOneTimeEvent
}

@OptIn(ExperimentalUuidApi::class)
class AgendaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val agendaRepository: IAgendaRepository,
    private val imageCompressor: IImageCompressor,
    networkManager: INetworkManager,
    private val sessionManager: ISessionManager,
) : ViewModel() {
    private val routeArguments = savedStateHandle.toRoute<AgendaDetails>()

    private val _screenStateFlow =
        MutableStateFlow(
            AgendaDetailsScreenState(
                agendaItem =
                    when (routeArguments.type) {
                        AgendaType.TASK -> Task.EMPTY
                        AgendaType.EVENT -> Event.EMPTY
                        AgendaType.REMINDER -> Reminder.EMPTY
                    },
                isEdit = routeArguments.isEdit,
            ),
        )
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val eventsChannel = Channel<AgendaDetailsOneTimeEvent>()
    val eventsFlow = eventsChannel.receiveAsFlow()

    private val deletedPhotoKeys = mutableListOf<String>()

    init {
        networkManager
            .observeHasConnection()
            .onEach { hasConnection ->
                _screenStateFlow.update { it.copy(hasNetwork = hasConnection) }
            }.launchIn(viewModelScope)
        getAgenda()
    }

    private fun getAgenda() {
        viewModelScope.launch {
            when (routeArguments.type) {
                AgendaType.TASK -> agendaRepository.getTask(taskId = routeArguments.agendaId)
                AgendaType.EVENT -> agendaRepository.getEvent(eventId = routeArguments.agendaId)
                AgendaType.REMINDER -> agendaRepository.getReminder(reminderId = routeArguments.agendaId)
            }.onSuccess { agendaItem ->
                when (agendaItem) {
                    is Task, is Reminder ->
                        _screenStateFlow.update {
                            it.copy(agendaItem = agendaItem)
                        }

                    is Event -> {
                        val userId = sessionManager.getUserId()
                        val eventIsGoing = agendaItem.attendees.firstOrNull { it.userId == userId }?.isGoing ?: true
                        _screenStateFlow.update {
                            it.copy(
                                agendaItem = agendaItem,
                                photos =
                                    agendaItem.photos.map { photo ->
                                        DetailsPhoto.RemotePhoto(photo)
                                    },
                                isCreator = agendaItem.isUserEventCreator,
                                eventIsGoing = eventIsGoing,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AgendaDetailsScreenEvent) {
        val item = screenStateFlow.value.agendaItem
        when (event) {
            AgendaDetailsScreenEvent.CloseEditText -> _screenStateFlow.update { it.copy(detailsEditTextType = null) }
            AgendaDetailsScreenEvent.OnCloseClick -> {}
            AgendaDetailsScreenEvent.OnBottomTextClick ->
                if (item is Event && !item.isUserEventCreator) {
                    toggleIsGoing()
                } else {
                    deleteAgendaItem()
                }
            is AgendaDetailsScreenEvent.OnDescChange -> updateDesc(event.newDesc)
            AgendaDetailsScreenEvent.OnDescClick ->
                _screenStateFlow.update {
                    it.copy(
                        detailsEditTextType = DetailsEditTextType.DESCRIPTION,
                    )
                }
            AgendaDetailsScreenEvent.OnEditClick -> _screenStateFlow.update { it.copy(isEdit = true) }
            is AgendaDetailsScreenEvent.OnRemindAtChange -> updateRemindAt(event.newType)
            AgendaDetailsScreenEvent.OnSaveClick -> saveAgenda()
            is AgendaDetailsScreenEvent.OnStartDateChange -> updateStartDate(event.newDate)
            is AgendaDetailsScreenEvent.OnStartTimeChange -> updateStartTime(event.newHour, event.newMinute)
            is AgendaDetailsScreenEvent.OnTitleChange -> updateTitle(event.newTitle)
            AgendaDetailsScreenEvent.OnTitleClick -> _screenStateFlow.update { it.copy(detailsEditTextType = DetailsEditTextType.TITLE) }
            is AgendaDetailsScreenEvent.OnAttendeeAdd -> addAttendee(event.email)
            is AgendaDetailsScreenEvent.OnAttendeeDelete -> deleteAttendee(event.id)
            is AgendaDetailsScreenEvent.OnAttendeeTabChange -> _screenStateFlow.update { it.copy(curTab = event.newTab) }
            is AgendaDetailsScreenEvent.OnEndDateChange -> updateEndDate(event.newDate)
            is AgendaDetailsScreenEvent.OnEndTimeChange -> updateEndTime(event.newHour, event.newMinute)
            is AgendaDetailsScreenEvent.OnAddPhoto -> addPhoto(event.uri)
            is AgendaDetailsScreenEvent.OnPhotoClick -> _screenStateFlow.update { it.copy(enlargedPhoto = event.photo) }
            AgendaDetailsScreenEvent.CloseLargePhoto -> _screenStateFlow.update { it.copy(enlargedPhoto = null) }
            is AgendaDetailsScreenEvent.OnPhotoDelete -> deletePhoto(key = event.key)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun addPhoto(uri: Uri) {
        _screenStateFlow.update { it.copy(photos = it.photos + DetailsPhoto.LocalPhoto(uri = uri, key = Uuid.random().toString())) }
    }

    private fun deletePhoto(key: String) {
        val newPhotos = screenStateFlow.value.photos.toMutableList()
        val isPhotoRemoved =
            newPhotos.removeIf {
                when (it) {
                    is DetailsPhoto.RemotePhoto -> it.photo.key
                    is DetailsPhoto.LocalPhoto -> it.key
                } == key
            }

        if (isPhotoRemoved) {
            deletedPhotoKeys.add(key)
            _screenStateFlow.update {
                it.copy(photos = newPhotos, enlargedPhoto = null)
            }
        }
    }

    private fun addAttendee(email: String) {
        val agendaItem = screenStateFlow.value.agendaItem

        if (agendaItem !is Event) {
            return
        }

        if (agendaItem.attendees.any { it.email == email }) {
            return
        }

        viewModelScope.launch {
            agendaRepository.getAttendee(email = email, eventId = agendaItem.id, from = agendaItem.from).onSuccess {
                it?.let { newAttendee ->
                    _screenStateFlow.update { state ->
                        state.copy(agendaItem = agendaItem.copy(attendees = agendaItem.attendees + newAttendee))
                    }
                }
            }
        }
    }

    private fun deleteAttendee(id: String) {
        val agendaItem = screenStateFlow.value.agendaItem

        if (agendaItem !is Event) {
            return
        }

        val newAttendees = agendaItem.attendees.toMutableList()
        if (newAttendees.removeIf { it.userId == id }) {
            _screenStateFlow.update { it.copy(agendaItem = agendaItem.copy(attendees = newAttendees)) }
        }
    }

    private fun toggleIsGoing() {
        val agendaItem = screenStateFlow.value.agendaItem

        if (agendaItem !is Event) {
            return
        }

        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            val newAttendees = agendaItem.attendees.toMutableList()
            val attendeeIndex = newAttendees.indexOfFirst { it.userId == userId }
            if (attendeeIndex >= 0) {
                val newAttendee = newAttendees[attendeeIndex].copy(isGoing = !newAttendees[attendeeIndex].isGoing)
                newAttendees[attendeeIndex] = newAttendee
                _screenStateFlow.update {
                    it.copy(
                        agendaItem = agendaItem.copy(attendees = newAttendees),
                        eventIsGoing = newAttendee.isGoing,
                    )
                }
            }
        }
    }

    private fun deleteAgendaItem() {
        viewModelScope.launch {
            agendaRepository.deleteAgenda(screenStateFlow.value.agendaItem).onSuccess {
                eventsChannel.send(AgendaDetailsOneTimeEvent.OnDeleteSuccess)
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
        val newDateTime =
            LocalDateTime(agendaItem.getStartTime().toLocalDateTime().date, LocalTime(hour = hour, minute = minute))
                .toInstant(
                    TimeZone.currentSystemDefault(),
                ).toEpochMilliseconds()
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(startTime = newDateTime))
        }
    }

    private fun updateStartDate(newDate: LocalDate) {
        val agendaItem = screenStateFlow.value.agendaItem
        val newDateTime =
            LocalDateTime(newDate, agendaItem.getStartTime().toLocalDateTime().time)
                .toInstant(
                    TimeZone.currentSystemDefault(),
                ).toEpochMilliseconds()
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(startTime = newDateTime))
        }
    }

    private fun updateEndTime(
        hour: Int,
        minute: Int,
    ) {
        val agendaItem = screenStateFlow.value.agendaItem

        if (agendaItem !is Event) {
            return
        }

        val newDateTime =
            LocalDateTime(agendaItem.to.toLocalDateTime().date, LocalTime(hour = hour, minute = minute))
                .toInstant(
                    TimeZone.currentSystemDefault(),
                ).toEpochMilliseconds()
        _screenStateFlow.update {
            it.copy(agendaItem = agendaItem.copy(to = newDateTime))
        }
    }

    private fun updateEndDate(newDate: LocalDate) {
        val agendaItem = screenStateFlow.value.agendaItem

        if (agendaItem !is Event) {
            return
        }

        val newDateTime =
            LocalDateTime(newDate, agendaItem.to.toLocalDateTime().time)
                .toInstant(
                    TimeZone.currentSystemDefault(),
                ).toEpochMilliseconds()
        _screenStateFlow.update {
            it.copy(agendaItem = agendaItem.copy(to = newDateTime))
        }
    }

    private fun updateRemindAt(newRemindAt: RemindAtType) {
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(remindAt = newRemindAt))
        }
    }

    private fun saveAgenda() {
        updateRemoteAgenda()
    }

    private fun updateRemoteAgenda() {
        viewModelScope.launch {
            val agendaItem = screenStateFlow.value.agendaItem
            var skippedImageCount = 0

            when (agendaItem) {
                is Task -> agendaRepository.updateTask(agendaItem)
                is Reminder -> agendaRepository.updateReminder(agendaItem)
                is Event -> {
                    val eventIsGoing = screenStateFlow.value.eventIsGoing
                    val localPhotos = screenStateFlow.value.photos.filterIsInstance<DetailsPhoto.LocalPhoto>()
                    val compressedPhotos =
                        localPhotos
                            .mapNotNull {
                                imageCompressor.compressImage(
                                    contentUri = it.uri,
                                    compressionThreshold = 1024L * 1024,
                                )
                            }.filter { it.size <= 1024 * 1024 }
                    skippedImageCount = localPhotos.size - compressedPhotos.size

                    agendaRepository.updateEvent(
                        event = agendaItem,
                        deletedPhotoKeys = deletedPhotoKeys,
                        isGoing = eventIsGoing,
                        photos = compressedPhotos,
                    )
                }
            }.onSuccess {
                if (it is Event) {
                    _screenStateFlow.update { state ->
                        state.copy(agendaItem = it, photos = it.photos.map { photo -> DetailsPhoto.RemotePhoto(photo) }, isEdit = false)
                    }

                    if (skippedImageCount > 0) {
                        eventsChannel.send(
                            AgendaDetailsOneTimeEvent.OnPhotoSkipped(
                                skippedImageCount,
                            ),
                        )
                    }
                } else {
                    _screenStateFlow.update { state ->
                        state.copy(isEdit = false)
                    }
                }
            }
        }
    }
}
