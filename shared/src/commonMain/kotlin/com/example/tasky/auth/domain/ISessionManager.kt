package com.example.tasky.auth.domain

import kotlinx.coroutines.flow.Flow

interface ISessionManager {
    val isLoginFlow: Flow<Boolean>

    suspend fun getFullName(): String?

    suspend fun getUserId(): String?
}
