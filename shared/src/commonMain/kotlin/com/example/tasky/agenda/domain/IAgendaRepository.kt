package com.example.tasky.agenda.domain

import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.domain.model.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface IAgendaRepository {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<List<AgendaItem>, DataError.Remote>

    fun getAgendaFlow(timeStamp: Long): Flow<List<AgendaItem>>

    suspend fun deleteAgenda(agendaItem: AgendaItem): ResultWrapper<Unit, DataError.Remote>

    suspend fun updateTask(task: Task): ResultWrapper<Unit, DataError.Remote>

    suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, DataError.Remote>

    suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<Event, DataError.Remote>

    suspend fun createTask(task: Task): ResultWrapper<Unit, DataError.Remote>

    suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, DataError.Remote>

    suspend fun createEvent(event: Event): ResultWrapper<Event, DataError.Remote>

    suspend fun getTask(taskId: String): ResultWrapper<Task, DataError.Remote>

    suspend fun getEvent(eventId: String): ResultWrapper<Event, DataError.Remote>

    suspend fun getReminder(reminderId: String): ResultWrapper<Reminder, DataError.Remote>

    suspend fun getAttendee(
        email: String,
        eventId: String,
        from: Long,
    ): ResultWrapper<Attendee?, DataError.Remote>

    suspend fun syncAgenda()
}
