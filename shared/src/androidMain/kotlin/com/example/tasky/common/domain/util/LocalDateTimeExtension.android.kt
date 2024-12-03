package com.example.tasky.common.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(isoFormat: String): String {
    return DateTimeFormatter.ofPattern(isoFormat).format(this.toJavaLocalDateTime())
}

actual fun LocalDate.format(isoFormat: String): String {
    return DateTimeFormatter.ofPattern(isoFormat).format(this.toJavaLocalDate())
}
