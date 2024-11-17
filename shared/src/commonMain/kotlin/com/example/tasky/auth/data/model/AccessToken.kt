package com.example.tasky.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenBody(
    val refreshToken: String,
    val userId: String,
)

@Serializable
data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long,
)
