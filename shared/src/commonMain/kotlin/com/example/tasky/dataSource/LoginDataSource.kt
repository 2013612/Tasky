package com.example.tasky.dataSource

import com.example.tasky.manager.HttpManager
import com.example.tasky.model.BaseError
import com.example.tasky.model.DataError
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.login.Login
import com.example.tasky.model.login.LoginBody
import com.example.tasky.model.login.LoginResponse
import com.example.tasky.model.login.Register
import com.example.tasky.model.login.RegisterBody
import com.example.tasky.util.toResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import kotlin.coroutines.cancellation.CancellationException

class LoginDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse, BaseError> =
        try {
            val response =
                httpClient.post(Login()) {
                    setBody(loginBody)
                }

            response.toResult<LoginResponse>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Throwable(e)
            ResultWrapper.Error(DataError.Remote.UNKNOWN)
        }

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, BaseError> =
        try {
            val response =
                httpClient.post(Register()) {
                    setBody(registerBody)
                }

            response.toResult<Unit>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            Throwable(e)
            ResultWrapper.Error(DataError.Remote.UNKNOWN)
        }
}
