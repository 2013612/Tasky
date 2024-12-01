package com.example.tasky.android.agenda.presentation.details.model

import com.example.tasky.android.common.presentation.utils.UiText

sealed interface AgendaDetailsOneTimeEvent {
    data object OnDeleteSuccess : AgendaDetailsOneTimeEvent

    data class OnPhotoSkipped(
        val skippedPhotoCount: Int,
    ) : AgendaDetailsOneTimeEvent

    data class OnAddAttendee(
        val name: String,
    ) : AgendaDetailsOneTimeEvent

    data class OnAddDuplicateAttendee(
        val name: String,
    ) : AgendaDetailsOneTimeEvent

    data class OnAddAttendeeFail(
        val email: String,
        val error: UiText,
    ) : AgendaDetailsOneTimeEvent

    data class OnRemoveAttendee(
        val name: String,
    ) : AgendaDetailsOneTimeEvent
}
