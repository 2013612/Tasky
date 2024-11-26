package com.example.tasky.auth.domain.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class StringExtensionTest {
    @Test
    fun `test getAvatarDisplayName 2 word`() {
        val name = "abc def"
        val actual = name.getAvatarDisplayName()

        assertThat(actual).isEqualTo("ad")
    }

    @Test
    fun `test getAvatarDisplayName with prefix space`() {
        val name = "   abc def"
        val actual = name.getAvatarDisplayName()

        assertThat(actual).isEqualTo("ad")
    }

    @Test
    fun `test getAvatarDisplayName with suffix space`() {
        val name = "abc def    "
        val actual = name.getAvatarDisplayName()

        assertThat(actual).isEqualTo("ad")
    }

    @Test
    fun `test getAvatarDisplayName empty string`() {
        val name = ""
        val actual = name.getAvatarDisplayName()

        assertThat(actual).isEqualTo("")
    }

    @Test
    fun `test getAvatarDisplayName 1 word`() {
        val name = "abc"
        val actual = name.getAvatarDisplayName()

        assertThat(actual).isEqualTo("ab")
    }

    @Test
    fun `test getAvatarDisplayName 3 word`() {
        val name = "abc def gef"
        val actual = name.getAvatarDisplayName()

        assertThat(actual).isEqualTo("ag")
    }
}
