package com.example.tasky.common.model

sealed interface ResultWrapper<out D, out E : BaseError> {
    data class Success<out D>(
        val data: D,
    ) : ResultWrapper<D, Nothing>

    data class Error<out E : BaseError>(
        val error: E,
    ) : ResultWrapper<Nothing, E>
}

inline fun <T, E : BaseError, R> ResultWrapper<T, E>.map(map: (T) -> R): ResultWrapper<R, E> =
    when (this) {
        is ResultWrapper.Error -> ResultWrapper.Error(error)
        is ResultWrapper.Success -> ResultWrapper.Success(map(data))
    }

fun <T, E : BaseError> ResultWrapper<T, E>.asEmptyDataResult(): EmptyResultWrapper<E> = map { }

inline fun <T, E : BaseError> ResultWrapper<T, E>.onSuccess(action: (T) -> Unit): ResultWrapper<T, E> =
    when (this) {
        is ResultWrapper.Error -> this
        is ResultWrapper.Success -> {
            action(data)
            this
        }
    }

inline fun <T, E : BaseError> ResultWrapper<T, E>.onError(action: (E) -> Unit): ResultWrapper<T, E> =
    when (this) {
        is ResultWrapper.Error -> {
            action(error)
            this
        }
        is ResultWrapper.Success -> this
    }

typealias EmptyResultWrapper<E> = ResultWrapper<Unit, E>
