package com.example.tasky.model.login

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/login")
class Login

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
