package com.example.tasky.dataSource

import com.example.tasky.manager.HttpManager
import com.example.tasky.model.ErrorResponse
import com.example.tasky.model.ResultWrapper
import com.example.tasky.model.login.Login
import com.example.tasky.model.login.LoginBody
import com.example.tasky.model.login.LoginResponse
import com.example.tasky.util.isSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class LoginDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse> {
        return try {
            val response =
                httpClient.post(Login()) {
                    setBody(loginBody)
                }

            if (!response.isSuccess()) {
                return ResultWrapper.Error(response.body<ErrorResponse>())
            }

            ResultWrapper.Success(response.body<LoginResponse>())
        } catch (e: Exception) {
            Throwable(e)
            ResultWrapper.Error(ErrorResponse.unknown)
        }
    }
}
