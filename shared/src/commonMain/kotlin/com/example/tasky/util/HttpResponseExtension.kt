package com.example.tasky.util

import io.ktor.client.statement.HttpResponse

fun HttpResponse.isSuccess(): Boolean = status.value in 200..299
