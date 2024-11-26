package com.example.tasky.common.domain.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(timeZone)
