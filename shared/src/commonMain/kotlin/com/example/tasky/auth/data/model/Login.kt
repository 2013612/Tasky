package com.example.tasky.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long,
)
