package com.example.tasky.dataSource

import com.example.tasky.manager.HttpManager
import com.example.tasky.model.BaseError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.agenda.Agenda
import com.example.tasky.model.agenda.EventPath
import com.example.tasky.model.agenda.GetAgendaResponse
import com.example.tasky.model.agenda.ReminderPath
import com.example.tasky.model.agenda.TaskPath
import com.example.tasky.model.agenda.UpdateEventBody
import com.example.tasky.model.agenda.UpdateReminderBody
import com.example.tasky.model.agenda.UpdateTaskBody
import com.example.tasky.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.put
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

    suspend fun updateEvent(body: UpdateEventBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put(EventPath()) {
                setBody(body)
            }
        }

    suspend fun updateReminder(body: UpdateReminderBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.put(ReminderPath()) {
                setBody(body)
            }
        }
}
