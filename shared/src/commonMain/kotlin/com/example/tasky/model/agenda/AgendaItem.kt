package com.example.tasky.model.agenda

import kotlinx.serialization.Serializable

@Serializable
sealed class AgendaItem {
    abstract val id: Int
}

@Serializable
data class Event(
    override val id: Int,
) : AgendaItem()

@Serializable
data class Task(
    override val id: Int,
) : AgendaItem()

@Serializable
data class Reminder(
    override val id: Int,
) : AgendaItem()
