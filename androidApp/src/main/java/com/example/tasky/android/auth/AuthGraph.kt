package com.example.tasky.android.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.tasky.android.auth.screen.Login
import com.example.tasky.android.auth.screen.loginScreen
import com.example.tasky.android.auth.screen.navigateToRegister
import com.example.tasky.android.auth.screen.registerScreen
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
