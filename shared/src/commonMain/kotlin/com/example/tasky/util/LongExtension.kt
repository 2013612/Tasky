package com.example.tasky.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDateTime(): LocalDateTime =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
