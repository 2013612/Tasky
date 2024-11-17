package com.example.tasky.auth.domain

import com.example.tasky.common.data.model.BaseError
import com.example.tasky.common.domain.model.ResultWrapper

interface ILoginRepository {
    suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<Boolean, BaseError>

    suspend fun logout(): ResultWrapper<Unit, BaseError>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): ResultWrapper<Unit, BaseError>
}
