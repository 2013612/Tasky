package com.example.tasky.agenda.domain.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

enum class RemindAtType(
    val duration: Duration,
) {
    TEN_MINUTE(10.minutes),
    THIRTY_MINUTE(30.minutes),
    ONE_HOUR(1.hours),
    SIX_HOUR(6.hours),
    ONE_DAY(1.days),
}

fun getRemindAtType(
    startTime: Long,
    remindAt: Long,
): RemindAtType =
    RemindAtType.entries.firstOrNull { it.duration.toLong(DurationUnit.MILLISECONDS) == startTime - remindAt }
        ?: RemindAtType.TEN_MINUTE
