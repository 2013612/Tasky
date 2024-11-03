package com.example.tasky.android.utils

interface UiEvent<T> {
    val data: T
    val onConsume: () -> Unit
}
