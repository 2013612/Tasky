package com.example.tasky.model.agenda

import kotlinx.serialization.Serializable

@Serializable
sealed class AgendaItem {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val remindAt: Long
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
) : AgendaItem()

@Serializable
data class Attendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

@Serializable
data class Photo(
    val key: String,
    val url: String,
)

@Serializable
data class Task(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long,
    override val remindAt: Long,
    val isDone: Boolean,
) : AgendaItem()

@Serializable
data class Reminder(
    override val id: String,
    override val title: String,
    override val description: String,
    val time: Long,
    override val remindAt: Long,
) : AgendaItem()
