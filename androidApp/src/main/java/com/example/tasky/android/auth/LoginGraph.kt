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
object LoginGraph

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation<LoginGraph>(startDestination = Login) {
        loginScreen(
            navigateToSignUp = navController::navigateToRegister,
        )
        registerScreen(
            navigateUp = navController::navigateUp,
        )
    }
}

fun NavController.navigateToLoginGraph() {
    navigate(LoginGraph)
}
