package com.example.tasky.login.domain

import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.login.data.model.LoginResponse
import com.example.tasky.login.data.model.RegisterBody

interface ILoginRepository {
    suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<LoginResponse, BaseError>

    suspend fun logout(): ResultWrapper<Unit, BaseError>

    suspend fun register(registerBody: RegisterBody): ResultWrapper<Unit, BaseError>
}
