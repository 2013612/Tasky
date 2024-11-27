package com.example.tasky.auth.domain

interface ISessionManager {
    suspend fun getFullName(): String?

    suspend fun getUserId(): String?
}
