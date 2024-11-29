package com.example.tasky.android.auth.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.tasky.android.auth.presentation.login.screen.Login
import com.example.tasky.android.auth.presentation.login.screen.loginScreen
import com.example.tasky.android.auth.presentation.register.screen.navigateToRegister
import com.example.tasky.android.auth.presentation.register.screen.registerScreen
import kotlinx.serialization.Serializable

@Serializable
object AuthGraph

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation<AuthGraph>(startDestination = Login) {
        loginScreen(
            navigateToSignUp = navController::navigateToRegister,
        )
        registerScreen(
            navigateUp = navController::navigateUp,
        )
    }
}

fun NavController.navigateToAuthGraph() {
    navigate(AuthGraph)
}
