package com.example.tasky.android.agenda.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tasky.android.agenda.components.details.DetailsEditTextType
import com.example.tasky.android.agenda.components.details.DetailsPhoto
import com.example.tasky.android.agenda.screen.AgendaDetails
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenEvent
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenState
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenType
import com.example.tasky.android.utils.IImageCompressor
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import com.example.tasky.model.agenda.UpdateEventBody
import com.example.tasky.model.agenda.copy
import com.example.tasky.model.onSuccess
import com.example.tasky.repository.IAgendaRepository
import com.example.tasky.util.toLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AgendaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val agendaRepository: IAgendaRepository,
    private val imageCompressor: IImageCompressor,
) : ViewModel() {
    private val routeArguments = savedStateHandle.toRoute<AgendaDetails>()

    private val _screenStateFlow =
        MutableStateFlow(
            AgendaDetailsScreenState(
                agendaItem =
                    when (routeArguments.type) {
                        AgendaDetailsScreenType.TASK -> Task.EMPTY
                        AgendaDetailsScreenType.EVENT -> Event.EMPTY
                        AgendaDetailsScreenType.REMINDER -> Reminder.EMPTY
                    },
                isEdit = routeArguments.isEdit,
            ),
        )
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val _isDeleteSuccess = MutableStateFlow(false)
    val isDeleteSuccess = _isDeleteSuccess.asStateFlow()

    private val _skippedImageCountFlow = MutableStateFlow(0)
    val skippedImageCountFlow = _skippedImageCountFlow.asStateFlow()

    private val deletedPhotoKeys = mutableListOf<String>()

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
            is AgendaDetailsScreenEvent.OnRemindAtChange -> updateRemindAt(event.newRemindAtTime)
            AgendaDetailsScreenEvent.OnSaveClick -> saveUpdate()
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
        // TODO
    }

    private fun deleteAttendee(id: String) {
        // TODO
    }

    private fun toggleIsGoing() {
        viewModelScope.launch {
            // TODO
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
        val newDateTime =
            LocalDateTime(agendaItem.getStartTime().toLocalDateTime().date, LocalTime(hour = hour, minute = minute))
                .toInstant(
                    TimeZone.currentSystemDefault(),
                ).toEpochMilliseconds()
        val remindAtDiff = agendaItem.getStartTime() - agendaItem.remindAt
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(startTime = newDateTime, remindAt = newDateTime - remindAtDiff))
        }
    }

    private fun updateStartDate(newDate: LocalDate) {
        val agendaItem = screenStateFlow.value.agendaItem
        val newDateTime =
            LocalDateTime(newDate, agendaItem.getStartTime().toLocalDateTime().time)
                .toInstant(
                    TimeZone.currentSystemDefault(),
                ).toEpochMilliseconds()
        val remindAtDiff = agendaItem.getStartTime() - agendaItem.remindAt
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(startTime = newDateTime, remindAt = newDateTime - remindAtDiff))
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

    private fun updateRemindAt(newRemindAt: Long) {
        _screenStateFlow.update {
            it.copy(agendaItem = it.agendaItem.copy(remindAt = it.agendaItem.getStartTime() - newRemindAt))
        }
    }

    private fun saveUpdate() {
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
                                    it.uri,
                                    1024L,
                                )
                            }.filter { it.size <= 1024 }
                    skippedImageCount = localPhotos.size - compressedPhotos.size

                    agendaRepository.updateEvent(
                        body =
                            UpdateEventBody(
                                event = agendaItem,
                                deletedPhotoKeys = deletedPhotoKeys,
                                isGoing = eventIsGoing,
                                photos = compressedPhotos,
                            ),
                    )
                }
            }.onSuccess {
                if (it is Event) {
                    _screenStateFlow.update { state ->
                        state.copy(agendaItem = it, photos = it.photos.map { photo -> DetailsPhoto.RemotePhoto(photo) }, isEdit = false)
                    }
                    _skippedImageCountFlow.update { skippedImageCount }
                } else {
                    _screenStateFlow.update { state ->
                        state.copy(isEdit = false)
                    }
                }
            }
        }
    }
}
