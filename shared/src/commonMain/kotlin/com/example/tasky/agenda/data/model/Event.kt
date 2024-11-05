package com.example.tasky.agenda.data.model

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/event")
class EventPath

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
    val attendees: List<Attendee>,
    val photos: List<Photo>,
)

@Serializable
data class Attendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
) {
    companion object {
        val DUMMY_LIST =
            listOf(
                Attendee(
                    email = "attendee1@example.com",
                    fullName = "Attendee 1 Name",
                    userId = "user123",
                    eventId = "event123",
                    isGoing = true,
                    remindAt = 1678886400000,
                ),
                Attendee(
                    email = "attendee2@example.com",
                    fullName = "Attendee 2 Name",
                    userId = "user456",
                    eventId = "event123",
                    isGoing = false,
                    remindAt = 1678886400000,
                ),
            )
    }
}

@Serializable
data class Photo(
    val key: String,
    val url: String,
) {
    companion object {
        val DUMMY_LIST =
            listOf(
                Photo(
                    key = "photoKey1",
                    url = "https://example.com/photo1.jpg",
                ),
                Photo(
                    key = "photoKey2",
                    url = "https://example.com/photo2.jpg",
                ),
            )
    }
}

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

data class CreateEventBody(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>,
    val photos: List<ByteArray>,
)
