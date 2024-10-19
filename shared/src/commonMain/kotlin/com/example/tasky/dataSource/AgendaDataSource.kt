package com.example.tasky.dataSource

import com.example.tasky.manager.HttpManager
import com.example.tasky.model.BaseError
import com.example.tasky.model.DataError
import com.example.tasky.model.ErrorResponse
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.agenda.Agenda
import com.example.tasky.model.agenda.GetAgendaResponse
import com.example.tasky.util.isSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.parameter
import kotlin.coroutines.cancellation.CancellationException

class AgendaDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun getAgenda(timeStamp: Long): ResultWrapper<GetAgendaResponse, BaseError> {
        return try {
            val response =
                httpClient.get(Agenda()) {
                    parameter("time", timeStamp)
                }

            if (!response.isSuccess()) {
                val errorResponse = response.body<ErrorResponse>()
                println("get agenda fail: ${errorResponse.message}")
                return ResultWrapper.Error(DataError.Remote.UNKNOWN)
            }

            ResultWrapper.Success(response.body<GetAgendaResponse>())
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Throwable(e)
            ResultWrapper.Error(DataError.Remote.UNKNOWN)
        }
    }
}
