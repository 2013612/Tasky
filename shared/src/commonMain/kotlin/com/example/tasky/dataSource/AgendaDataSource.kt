package com.example.tasky.dataSource

import com.example.tasky.manager.HttpManager
import com.example.tasky.model.BaseError
import com.example.tasky.model.DataError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.agenda.Agenda
import com.example.tasky.model.agenda.EventPath
import com.example.tasky.model.agenda.GetAgendaResponse
import com.example.tasky.util.toResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.parameter
import kotlin.coroutines.cancellation.CancellationException

class AgendaDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError> =
        try {
            val response =
                httpClient.get(Agenda()) {
                    parameter("time", timeStamp)
                }

            response.toResult<GetAgendaResponse>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Throwable(e)
            ResultWrapper.Error(DataError.Remote.UNKNOWN)
        }

    suspend fun deleteEvent(eventId: String): ResultWrapper<Unit, BaseError> =
        try {
            val response =
                httpClient.delete(EventPath()) {
                    parameter("eventId", eventId)
                }

            response.toResult<Unit>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Throwable(e)
            ResultWrapper.Error(DataError.Remote.UNKNOWN)
        }
}
