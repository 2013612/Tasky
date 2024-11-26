package com.example.tasky.commom.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.common.domain.util.toLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test

class LongExtensionTest {
    @Test
    fun `test toLocalDateTime`() {
        val timeStamp = 1732642253000L
        val actual = timeStamp.toLocalDateTime(TimeZone.UTC)

        val expect = LocalDateTime(2024, 11, 26, 17, 30, 53)

        assertThat(actual).isEqualTo(expect)
    }
}
