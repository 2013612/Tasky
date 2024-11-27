package com.example.tasky.auth.data

import com.example.tasky.auth.data.manager.SessionManager
import com.example.tasky.auth.data.model.LoginBody
import com.example.tasky.auth.data.model.RegisterBody
import com.example.tasky.auth.domain.IAuthRepository
import com.example.tasky.common.data.model.DataError
import com.example.tasky.common.domain.model.ResultWrapper
import com.example.tasky.common.domain.model.map
import com.example.tasky.common.domain.model.onSuccess

class AuthRepository(
    private val authDataSource: AuthDataSource = AuthDataSource(),
) : IAuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): ResultWrapper<Boolean, DataError.Remote> =
        authDataSource
            .login(LoginBody(email, password))
            .onSuccess {
                SessionManager.setLoginResponse(it)
            }.map { true }

    override suspend fun logout(): ResultWrapper<Unit, DataError.Remote> =
        authDataSource.logout().onSuccess {
            SessionManager.removeToken()
        }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ) = authDataSource.register(
        RegisterBody(fullName, email, password),
    )
}
