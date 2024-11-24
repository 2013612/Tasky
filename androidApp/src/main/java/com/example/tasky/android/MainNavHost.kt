package com.example.tasky.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.tasky.android.agenda.presentation.AgendaGraph
import com.example.tasky.android.agenda.presentation.agendaGraph
import com.example.tasky.android.auth.AuthGraph
import com.example.tasky.android.auth.authGraph

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    isLogin: Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination =
            if (isLogin) {
                AgendaGraph
            } else {
                AuthGraph
            },
        modifier = modifier,
    ) {
        authGraph(navController = navHostController)
        agendaGraph(navController = navHostController)
    }
}
