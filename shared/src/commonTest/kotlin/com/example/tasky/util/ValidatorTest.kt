package com.example.tasky.util

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.example.tasky.auth.domain.util.Validator
import kotlin.test.Test

class ValidatorTest {
    private val validator: Validator = Validator

    @Test
    fun `test validateEmail`() {
        val email = "abc@abc.com"

        val actual = validator.validateEmail(email)

        assertThat(actual).isTrue()
    }

    @Test
    fun `test validateEmail no at`() {
        val email = "abcabc.com"

        val actual = validator.validateEmail(email)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test validateEmail no dot`() {
        val email = "abc@abccom"

        val actual = validator.validateEmail(email)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test validateName`() {
        val name = "name name"

        val actual = validator.validateName(name)

        assertThat(actual).isTrue()
    }

    @Test
    fun `test validatePassword`() {
        val password = "aA!123456"

        val actual = validator.validatePassword(password)

        assertThat(actual).isTrue()
    }

    @Test
    fun `test validatePassword no lowercase`() {
        val password = "AA!123456"

        val actual = validator.validatePassword(password)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test validatePassword no uppercase`() {
        val password = "aa!123456"

        val actual = validator.validatePassword(password)

        assertThat(actual).isFalse()
    }

    @Test
    fun `test validatePassword not enough length`() {
        val password = "aA123456"

        val actual = validator.validatePassword(password)

        assertThat(actual).isFalse()
    }
}
