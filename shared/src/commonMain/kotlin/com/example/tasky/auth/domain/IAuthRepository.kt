package com.example.tasky.auth.domain

import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.domain.model.ResultWrapper

interface IAuthRepository {
    suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<Boolean, DataError.Remote>

    suspend fun logout(): ResultWrapper<Unit, DataError.Remote>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): ResultWrapper<Unit, DataError.Remote>
}
