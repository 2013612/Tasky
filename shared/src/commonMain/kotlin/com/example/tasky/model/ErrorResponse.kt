package com.example.tasky.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
)