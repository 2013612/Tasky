package com.example.tasky.android.agenda.presentation.details.model

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
        val error: String,
    ) : AgendaDetailsOneTimeEvent

    data class OnRemoveAttendee(
        val name: String,
    ) : AgendaDetailsOneTimeEvent
}
