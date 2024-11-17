package com.example.tasky.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    val fullName: String,
    val email: String,
    val password: String,
)
