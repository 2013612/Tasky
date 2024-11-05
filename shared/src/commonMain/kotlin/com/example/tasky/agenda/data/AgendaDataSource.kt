package com.example.tasky.agenda.data

import com.example.tasky.agenda.data.model.Agenda
import com.example.tasky.agenda.data.model.CreateEventBody
import com.example.tasky.agenda.data.model.CreateReminderBody
import com.example.tasky.agenda.data.model.CreateTaskBody
import com.example.tasky.agenda.data.model.EventPath
import com.example.tasky.agenda.data.model.GetAgendaResponse
import com.example.tasky.agenda.data.model.ReminderPath
import com.example.tasky.agenda.data.model.RemoteEvent
import com.example.tasky.agenda.data.model.RemoteReminder
import com.example.tasky.agenda.data.model.RemoteTask
import com.example.tasky.agenda.data.model.TaskPath
import com.example.tasky.agenda.data.model.UpdateEventBody
import com.example.tasky.agenda.data.model.UpdateReminderBody
import com.example.tasky.agenda.data.model.UpdateTaskBody
import com.example.tasky.common.manager.HttpManager
import com.example.tasky.common.model.BaseError
import com.example.tasky.common.model.ResultWrapper
import com.example.tasky.common.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody

class AgendaDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError> =
        safeCall {
            httpClient.get(Agenda()) {
                parameter("time", timeStamp)
            }
        }

    suspend fun deleteEvent(eventId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete(EventPath()) {
                parameter("eventId", eventId)
            }
        }

    suspend fun deleteTask(taskId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete(TaskPath()) {
                parameter("taskId", taskId)
            }
        }

    suspend fun deleteReminder(reminderId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete(ReminderPath()) {
                parameter("reminderId", reminderId)
            }
        }

    suspend fun updateTask(body: UpdateTaskBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put(TaskPath()) {
                setBody(body)
            }
        }

    suspend fun updateEvent(body: UpdateEventBody): ResultWrapper<RemoteEvent, BaseError> =
        safeCall {
            httpClient.put(EventPath()) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("id", body.id)
                            append("title", body.title)
                            append("description", body.description)
                            append("from", body.from)
                            append("to", body.to)
                            append("remindAt", body.remindAt)
                            append("attendeeIds", body.attendeeIds)
                            append("deletedPhotoKeys", body.deletedPhotoKeys)
                            append("isGoing", body.isGoing)
                            body.photos.forEachIndexed { index, bytes ->
                                append("photo$index", bytes)
                            }
                        },
                    ),
                )
            }
        }

    suspend fun updateReminder(body: UpdateReminderBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put(ReminderPath()) {
                setBody(body)
            }
        }

    suspend fun createTask(body: CreateTaskBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post(TaskPath()) {
                setBody(body)
            }
        }

    suspend fun createReminder(body: CreateReminderBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post(ReminderPath()) {
                setBody(body)
            }
        }

    suspend fun createEvent(body: CreateEventBody): ResultWrapper<RemoteEvent, BaseError> =
        safeCall {
            httpClient.post(EventPath()) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("id", body.id)
                            append("title", body.title)
                            append("description", body.description)
                            append("from", body.from)
                            append("to", body.to)
                            append("remindAt", body.remindAt)
                            append("attendeeIds", body.attendeeIds)
                            body.photos.forEachIndexed { index, bytes ->
                                append("photo$index", bytes)
                            }
                        },
                    ),
                )
            }
        }

    suspend fun getEvent(eventId: String): ResultWrapper<RemoteEvent, BaseError> =
        safeCall {
            httpClient.get(EventPath()) {
                parameter("eventId", eventId)
            }
        }

    suspend fun getTask(taskId: String): ResultWrapper<RemoteTask, BaseError> =
        safeCall {
            httpClient.get(TaskPath()) {
                parameter("taskId", taskId)
            }
        }

    suspend fun getReminder(reminderId: String): ResultWrapper<RemoteReminder, BaseError> =
        safeCall {
            httpClient.get(ReminderPath()) {
                parameter("reminderId", reminderId)
            }
        }
}