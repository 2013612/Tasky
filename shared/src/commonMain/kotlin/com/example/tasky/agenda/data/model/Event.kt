package com.example.tasky.agenda.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteEvent(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<RemoteAttendee>,
    val photos: List<RemotePhoto>,
)

@Serializable
data class RemoteAttendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

@Serializable
data class RemotePhoto(
    val key: String,
    val url: String,
)

@Serializable
data class UpdateEventBody(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>,
    val deletedPhotoKeys: List<String>,
    val isGoing: Boolean,
)

@Serializable
data class CreateEventBody(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>,
)

@Serializable
data class GetAttendeeResponse(
    val doesUserExist: Boolean,
    val attendee: GetAttendeeResponseAttendee?,
)

@Serializable
data class GetAttendeeResponseAttendee(
    val email: String,
    val fullName: String,
    val userId: String,
)
