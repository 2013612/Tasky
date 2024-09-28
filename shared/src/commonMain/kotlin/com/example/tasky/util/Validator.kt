package com.example.tasky.util

object Validator {
    private val emailAddressRegex =
        Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+",
        )

    fun validateEmail(email: String): Boolean = email.matches(emailAddressRegex)

    fun validateName(name: String): Boolean = name.length in 4..50

    fun validatePassword(password: String): Boolean =
        password.length >= 9 && password.any { it.isLowerCase() } && password.any { it.isUpperCase() }
}
