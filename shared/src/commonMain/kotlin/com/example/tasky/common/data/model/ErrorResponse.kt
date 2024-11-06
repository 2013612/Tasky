package com.example.tasky.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
) {
    companion object {
        val unknown = ErrorResponse("Unknown error")
    }
}
