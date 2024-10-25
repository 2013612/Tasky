package com.example.tasky.android.agenda

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.tasky.android.agenda.screen.Agenda
import com.example.tasky.android.agenda.screen.agendaScreen
import kotlinx.serialization.Serializable

@Serializable
object AgendaGraph

fun NavGraphBuilder.agendaGraph(navController: NavController) {
    navigation<AgendaGraph>(startDestination = Agenda) {
        agendaScreen(
            navigateToCreateEvent = { },
            navigateToCreateTask = { },
            navigateToCreateReminder = { },
            navigateToAgendaDetails = { },
            navigateToEditAgenda = { },
        )
    }
}

fun NavController.navigateToAgendaGraph() {
    navigate(AgendaGraph)
}
