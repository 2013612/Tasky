package com.example.tasky.login.domain

import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.login.data.model.LoginResponse

interface ILoginRepository {
    suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<LoginResponse, BaseError>

    suspend fun logout(): ResultWrapper<Unit, BaseError>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): ResultWrapper<Unit, BaseError>
}
