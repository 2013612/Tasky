package com.example.tasky.model.agenda

import kotlinx.serialization.Serializable

@Serializable
sealed class AgendaItem {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val remindAt: Long

    abstract fun getStartTime(): Long
}

fun AgendaItem.copy(
    title: String = this.title,
    description: String = this.description,
    remindAt: Long = this.remindAt,
    startTime: Long = this.getStartTime(),
): AgendaItem =
    when (this) {
        is Event -> this.copy(title = title, description = description, remindAt = remindAt, from = startTime)
        is Reminder -> this.copy(title = title, description = description, remindAt = remindAt, startTime = startTime)
        is Task -> this.copy(title = title, description = description, remindAt = remindAt, startTime = startTime)
    }

@Serializable
data class Event(
    override val id: String,
    override val title: String,
    override val description: String,
    val from: Long,
    val to: Long,
    override val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val photos: List<Photo>,
) : AgendaItem() {
    companion object {
        val DUMMY =
            Event(
                id = "event123",
                title = "Fixed Event Title",
                description = "Fixed Event Description",
                from = 1678886400000,
                to = 1678972800000,
                remindAt = 1678886400000,
                host = "user123",
                isUserEventCreator = true,
                attendees = Attendee.DUMMY_LIST,
                photos = Photo.DUMMY_LIST,
            )
        val EMPTY =
            Event(
                id = "",
                title = "",
                description = "",
                from = 0,
                to = 0,
                remindAt = 0,
                host = "",
                isUserEventCreator = false,
                attendees = emptyList(),
                photos = emptyList(),
            )
    }

    override fun getStartTime(): Long = from
}

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
data class Task(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long,
    override val remindAt: Long,
    val isDone: Boolean,
) : AgendaItem() {
    companion object {
        val DUMMY =
            Task(
                id = "1",
                title = "Project X",
                description = "Just work",
                time = 1678886400000,
                remindAt = 1678886400000,
                isDone = true,
            )
        val EMPTY = Task(id = "", title = "", description = "", time = 0, remindAt = 0, isDone = false)
    }

    override fun getStartTime(): Long = time
}

@Serializable
data class Reminder(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long,
    override val remindAt: Long,
) : AgendaItem() {
    companion object {
        val DUMMY =
            Reminder(
                id = "1",
                title = "Meeting",
                description = "Amet minim mollit non deserunt ullamco est",
                time = 1678886400000,
                remindAt = 1678886400000,
            )
        val EMPTY = Reminder(id = "", title = "", description = "", time = 0, remindAt = 0)
    }

    override fun getStartTime(): Long = time
}
