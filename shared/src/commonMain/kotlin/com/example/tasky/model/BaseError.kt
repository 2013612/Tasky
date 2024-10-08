package com.example.tasky.model

interface BaseError

sealed interface DataError : BaseError {
    enum class Remote : DataError {
        UNKNOWN,
    }
}
