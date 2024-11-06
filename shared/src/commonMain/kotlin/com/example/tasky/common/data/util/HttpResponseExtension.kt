package com.example.tasky.common.data.util

import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.domain.model.ResultWrapper
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

fun HttpResponse.isSuccess(): Boolean = status.value in 200..299

suspend inline fun <reified T> HttpResponse.toResult(): ResultWrapper<T, DataError.Remote> =
    when (status.value) {
        in 200..299 -> ResultWrapper.Success(body<T>())
        400 -> ResultWrapper.Error(DataError.Remote.BAD_REQUEST)
        401 -> ResultWrapper.Error(DataError.Remote.UNAUTHORIZED)
        403 -> ResultWrapper.Error(DataError.Remote.FORBIDDEN)
        404 -> ResultWrapper.Error(DataError.Remote.NOT_FOUND)
        408 -> ResultWrapper.Error(DataError.Remote.REQUEST_TIMEOUT)
        409 -> ResultWrapper.Error(DataError.Remote.CONFLICT)
        413 -> ResultWrapper.Error(DataError.Remote.PAYLOAD_TOO_LARGE)
        429 -> ResultWrapper.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> ResultWrapper.Error(DataError.Remote.SERVER_ERROR)
        else -> ResultWrapper.Error(DataError.Remote.UNKNOWN)
    }
