package com.example.tasky.login.data.model

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/register")
class Register

@Serializable
data class RegisterBody(
    val fullName: String,
    val email: String,
    val password: String,
)
