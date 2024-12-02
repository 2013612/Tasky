package com.example.tasky.android.agenda.presentation.details.model

import android.net.Uri
import com.example.tasky.agenda.domain.model.RemindAtType
import com.example.tasky.android.agenda.presentation.details.component.DetailsAttendeeSectionTabOption
import com.example.tasky.android.agenda.presentation.details.component.DetailsPhoto
import kotlinx.datetime.LocalDate

sealed interface AgendaDetailsScreenEvent {
    data object OnCloseClick : AgendaDetailsScreenEvent

    data object OnSaveClick : AgendaDetailsScreenEvent

    data object OnEditClick : AgendaDetailsScreenEvent

    data object OnTitleClick : AgendaDetailsScreenEvent

    data object OnDescClick : AgendaDetailsScreenEvent

    data object OnBottomTextClick : AgendaDetailsScreenEvent

    data object CloseEditText : AgendaDetailsScreenEvent

    data class CloseUnsavedDialog(val isCancel: Boolean) : AgendaDetailsScreenEvent

    data class OnTitleChange(
        val newTitle: String,
    ) : AgendaDetailsScreenEvent

    data class OnDescChange(
        val newDesc: String,
    ) : AgendaDetailsScreenEvent

    data class OnStartTimeChange(
        val newHour: Int,
        val newMinute: Int,
    ) : AgendaDetailsScreenEvent

    data class OnStartDateChange(
        val newDate: LocalDate,
    ) : AgendaDetailsScreenEvent

    data class OnEndTimeChange(
        val newHour: Int,
        val newMinute: Int,
    ) : AgendaDetailsScreenEvent

    data class OnEndDateChange(
        val newDate: LocalDate,
    ) : AgendaDetailsScreenEvent

    data class OnRemindAtChange(
        val newType: RemindAtType,
    ) : AgendaDetailsScreenEvent

    data class OnAttendeeTabChange(
        val newTab: DetailsAttendeeSectionTabOption,
    ) : AgendaDetailsScreenEvent

    data class OnAttendeeAdd(
        val email: String,
    ) : AgendaDetailsScreenEvent

    data class OnAttendeeDelete(
        val id: String,
    ) : AgendaDetailsScreenEvent

    data class OnAddPhoto(
        val uri: Uri,
    ) : AgendaDetailsScreenEvent

    data class OnPhotoClick(
        val photo: DetailsPhoto,
    ) : AgendaDetailsScreenEvent

    data object CloseLargePhoto : AgendaDetailsScreenEvent

    data class OnPhotoDelete(
        val key: String,
    ) : AgendaDetailsScreenEvent
}
