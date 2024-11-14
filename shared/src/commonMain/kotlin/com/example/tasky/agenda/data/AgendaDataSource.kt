package com.example.tasky.agenda.data

import com.example.tasky.agenda.data.mapper.toCreateEventBody
import com.example.tasky.agenda.data.mapper.toRemoteReminder
import com.example.tasky.agenda.data.mapper.toRemoteTask
import com.example.tasky.agenda.data.mapper.toUpdateEventBody
import com.example.tasky.agenda.data.model.CreateEventBody
import com.example.tasky.agenda.data.model.GetAgendaResponse
import com.example.tasky.agenda.data.model.GetAttendeeResponse
import com.example.tasky.agenda.data.model.RemoteEvent
import com.example.tasky.agenda.data.model.RemoteReminder
import com.example.tasky.agenda.data.model.RemoteTask
import com.example.tasky.agenda.data.model.SyncAgendaBody
import com.example.tasky.agenda.data.model.UpdateEventBody
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.data.util.safeCall
import com.example.tasky.common.domain.model.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.encodeToString

class AgendaDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError> =
        safeCall {
            httpClient.get("/agenda") {
                parameter("time", timeStamp)
            }
        }

    suspend fun deleteEvent(eventId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete("/event") {
                parameter("eventId", eventId)
            }
        }

    suspend fun deleteTask(taskId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete("/task") {
                parameter("taskId", taskId)
            }
        }

    suspend fun deleteReminder(reminderId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete("/reminder") {
                parameter("reminderId", reminderId)
            }
        }

    suspend fun updateTask(task: Task): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put("/task") {
                setBody(task.toRemoteTask())
            }
        }

    suspend fun updateTask(remoteTask: RemoteTask): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put("/task") {
                setBody(remoteTask)
            }
        }

    suspend fun updateEvent(
        event: Event,
        deletedPhotoKeys: List<String>,
        isGoing: Boolean,
        photos: List<ByteArray>,
    ): ResultWrapper<RemoteEvent, BaseError> = updateEvent(event.toUpdateEventBody(deletedPhotoKeys, isGoing), photos)

    suspend fun updateEvent(
        body: UpdateEventBody,
        photos: List<ByteArray>,
    ): ResultWrapper<RemoteEvent, BaseError> =
        safeCall {
            val updateEventJson = HttpManager.json.encodeToString(body)
            httpClient.submitFormWithBinaryData(
                url = "/event",
                formData =
                    formData {
                        photos.forEachIndexed { index, photoByteArray ->
                            append("photo$index", photoByteArray)
                            append(HttpHeaders.ContentDisposition, "filename=${body.id}_picture$index.jpg")
                        }
                        append(
                            "update_event_request",
                            updateEventJson,
                            Headers.build {
                                append(HttpHeaders.ContentType, "text/plain")
                                append(HttpHeaders.ContentDisposition, "form-data; name=\"update_event_request\"")
                            },
                        )
                    },
            ) {
                method = HttpMethod.Put
            }
        }

    suspend fun updateReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put("/reminder") {
                setBody(reminder.toRemoteReminder())
            }
        }

    suspend fun updateReminder(remoteReminder: RemoteReminder): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put("/reminder") {
                setBody(remoteReminder)
            }
        }

    suspend fun createTask(task: Task): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post("/task") {
                setBody(task.toRemoteTask())
            }
        }

    suspend fun createTask(remoteTask: RemoteTask): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post("/task") {
                setBody(remoteTask)
            }
        }

    suspend fun createReminder(reminder: Reminder): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post("/reminder") {
                setBody(reminder.toRemoteReminder())
            }
        }

    suspend fun createReminder(remoteReminder: RemoteReminder): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post("/reminder") {
                setBody(remoteReminder)
            }
        }

    suspend fun createEvent(event: Event): ResultWrapper<RemoteEvent, BaseError> = createEvent(event.toCreateEventBody())

    suspend fun createEvent(body: CreateEventBody): ResultWrapper<RemoteEvent, BaseError> =
        safeCall {
            val createEventJson = HttpManager.json.encodeToString(body)
            httpClient.submitFormWithBinaryData(
                url = "/event",
                formData =
                    formData {
                        append(
                            "create_event_request",
                            createEventJson,
                            Headers.build {
                                append(HttpHeaders.ContentType, "text/plain")
                                append(HttpHeaders.ContentDisposition, "form-data; name=\"create_event_request\"")
                            },
                        )
                    },
            ) {
                method = HttpMethod.Post
            }
        }

    suspend fun getEvent(eventId: String): ResultWrapper<RemoteEvent, BaseError> =
        safeCall {
            httpClient.get("/event") {
                parameter("eventId", eventId)
            }
        }

    suspend fun getTask(taskId: String): ResultWrapper<RemoteTask, BaseError> =
        safeCall {
            httpClient.get("/task") {
                parameter("taskId", taskId)
            }
        }

    suspend fun getReminder(reminderId: String): ResultWrapper<RemoteReminder, BaseError> =
        safeCall {
            httpClient.get("/reminder") {
                parameter("reminderId", reminderId)
            }
        }

    suspend fun getAttendee(email: String): ResultWrapper<GetAttendeeResponse, BaseError> =
        safeCall {
            httpClient.get("/attendee") {
                parameter("email", email)
            }
        }

    suspend fun deleteEventForAttendee(eventId: String): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.delete("/attendee") {
                parameter("eventId", eventId)
            }
        }

    suspend fun syncDeleteAgenda(
        deletedEventIds: List<String>,
        deletedTaskIds: List<String>,
        deletedReminderIds: List<String>,
    ): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post("/syncAgenda") {
                setBody(SyncAgendaBody(deletedEventIds, deletedTaskIds, deletedReminderIds))
            }
        }

    suspend fun getFullAgenda(): ResultWrapper<GetAgendaResponse, BaseError> =
        safeCall {
            httpClient.get("/fullAgenda")
        }
}
