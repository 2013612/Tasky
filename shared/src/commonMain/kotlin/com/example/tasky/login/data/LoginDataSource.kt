package com.example.tasky.login.data

import com.example.tasky.common.data.manager.HttpManager
import com.example.tasky.common.data.util.safeCall
import com.example.tasky.common.model.BaseError
import com.example.tasky.common.model.ResultWrapper
import com.example.tasky.login.data.model.Login
import com.example.tasky.login.data.model.LoginBody
import com.example.tasky.login.data.model.LoginResponse
import com.example.tasky.login.data.model.Register
import com.example.tasky.login.data.model.RegisterBody
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class LoginDataSource(
    private val httpClient: HttpClient = HttpManager.httpClient,
) {
    suspend fun login(loginBody: LoginBody): ResultWrapper<LoginResponse, BaseError> =
        safeCall {
            httpClient.post(Login()) {
                setBody(loginBody)
            }
        }

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, BaseError> =
        safeCall {
            httpClient.post(Register()) {
                setBody(registerBody)
            }
        }
}
