package com.example.tasky.login.data.model

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/authenticate")
class Authenticate

@Resource("/accessToken")
class AccessToken

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
