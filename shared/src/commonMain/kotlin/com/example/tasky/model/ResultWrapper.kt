package com.example.tasky.model

sealed interface ResultWrapper<out D> {
    data class Success<out D>(
        val data: D,
    ) : ResultWrapper<D>

    data class Error<out D>(
        val error: ErrorResponse,
    ) : ResultWrapper<D>
}
