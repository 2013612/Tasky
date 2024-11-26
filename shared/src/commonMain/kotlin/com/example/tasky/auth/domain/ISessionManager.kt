package com.example.tasky.auth.domain

import com.example.tasky.auth.domain.model.RefreshToken
import io.ktor.client.plugins.auth.providers.BearerTokens

interface ISessionManager {
    suspend fun loadTokens(): BearerTokens

    suspend fun loadAccessTokenBody(): RefreshToken?

    suspend fun updateAccessToken(
        newToken: String,
        expirationTimestamp: Long,
    )

    suspend fun removeToken()

    suspend fun getFullName(): String?

    suspend fun getUserId(): String?
}
