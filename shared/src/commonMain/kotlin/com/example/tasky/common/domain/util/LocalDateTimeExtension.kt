package com.example.tasky.common.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

expect fun LocalDateTime.format(isoFormat: String): String

expect fun LocalDate.format(isoFormat: String): String
