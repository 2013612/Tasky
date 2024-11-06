package com.example.tasky.agenda.domain.model

import com.example.tasky.agenda.data.model.CreateEventBody
import com.example.tasky.agenda.data.model.RemoteAttendee
import com.example.tasky.agenda.data.model.RemoteEvent
import com.example.tasky.agenda.data.model.RemotePhoto
import com.example.tasky.agenda.data.model.RemoteReminder
import com.example.tasky.agenda.data.model.RemoteTask
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlin.time.DurationUnit

@Serializable
sealed class AgendaItem {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val remindAt: RemindAtType

    abstract fun getStartTime(): Long
}

fun AgendaItem.copy(
    title: String = this.title,
    description: String = this.description,
    remindAt: RemindAtType = this.remindAt,
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
    override val remindAt: RemindAtType,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val photos: List<Photo>,
) : AgendaItem() {
    constructor(remoteEvent: RemoteEvent) : this(
        id = remoteEvent.id,
        title = remoteEvent.title,
        description = remoteEvent.description,
        from = remoteEvent.from,
        to = remoteEvent.to,
        remindAt = getRemindAtType(remoteEvent.from, remoteEvent.remindAt),
        host = remoteEvent.host,
        isUserEventCreator = remoteEvent.isUserEventCreator,
        attendees = remoteEvent.attendees.map { Attendee(it) },
        photos = remoteEvent.photos.map { Photo(it) },
    )

    fun toCreateEventBody(): CreateEventBody =
        CreateEventBody(
            id = id,
            title = title,
            description = description,
            from = from,
            to = to,
            remindAt = from - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
            attendeeIds = attendees.map { it.userId },
        )

    companion object {
        val DUMMY =
            Event(
                id = "event123",
                title = "Fixed Event Title",
                description = "Fixed Event Description",
                from = 1678886400000,
                to = 1678972800000,
                remindAt = RemindAtType.TEN_MINUTE,
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
                from = Clock.System.now().toEpochMilliseconds(),
                to = Clock.System.now().toEpochMilliseconds(),
                remindAt = RemindAtType.TEN_MINUTE,
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
    constructor(remoteAttendee: RemoteAttendee) : this(
        email = remoteAttendee.email,
        fullName = remoteAttendee.fullName,
        userId = remoteAttendee.userId,
        eventId = remoteAttendee.eventId,
        isGoing = remoteAttendee.isGoing,
        remindAt = remoteAttendee.remindAt,
    )

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
    constructor(remotePhoto: RemotePhoto) : this(
        key = remotePhoto.key,
        url = remotePhoto.url,
    )

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
    override val remindAt: RemindAtType,
    val isDone: Boolean,
) : AgendaItem() {
    constructor(
        task: RemoteTask,
    ) : this(
        id = task.id,
        title = task.title,
        description = task.description,
        time = task.time,
        remindAt = getRemindAtType(task.time, task.remindAt),
        isDone = task.isDone,
    )

    fun toRemoteTask(): RemoteTask =
        RemoteTask(
            id = this.id,
            title = this.title,
            description = this.description,
            time = this.time,
            remindAt = time - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
            isDone = this.isDone,
        )

    companion object {
        val DUMMY =
            Task(
                id = "Task 1",
                title = "Project X",
                description = "Just work",
                time = 1678886400000,
                remindAt = RemindAtType.TEN_MINUTE,
                isDone = true,
            )
        val EMPTY =
            Task(
                id = "",
                title = "",
                description = "",
                time = Clock.System.now().toEpochMilliseconds(),
                remindAt = RemindAtType.TEN_MINUTE,
                isDone = false,
            )
    }

    override fun getStartTime(): Long = time
}

@Serializable
data class Reminder(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long,
    override val remindAt: RemindAtType,
) : AgendaItem() {
    constructor(
        reminder: RemoteReminder,
    ) : this(
        id = reminder.id,
        title = reminder.title,
        description = reminder.description,
        time = reminder.time,
        remindAt = getRemindAtType(reminder.time, reminder.remindAt),
    )

    fun toRemoteReminder(): RemoteReminder =
        RemoteReminder(
            id = this.id,
            title = this.title,
            description = this.description,
            time = this.time,
            remindAt = time - remindAt.duration.toLong(DurationUnit.MILLISECONDS),
        )

    companion object {
        val DUMMY =
            Reminder(
                id = "Reminder 1",
                title = "Meeting",
                description = "Amet minim mollit non deserunt ullamco est",
                time = 1678886400000,
                remindAt = RemindAtType.TEN_MINUTE,
            )
        val EMPTY =
            Reminder(
                id = "",
                title = "",
                description = "",
                time = Clock.System.now().toEpochMilliseconds(),
                remindAt = RemindAtType.TEN_MINUTE,
            )
    }

    override fun getStartTime(): Long = time
}
