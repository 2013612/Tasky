package com.example.tasky.android.agenda.presentation.details.utils

import com.example.tasky.common.domain.util.toLocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

fun Long.getTitleTimeDisplay(): String {
    val dateTimeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

    return toLocalDateTime()
        .toJavaLocalDateTime()
        .format(dateTimeFormat)
}
