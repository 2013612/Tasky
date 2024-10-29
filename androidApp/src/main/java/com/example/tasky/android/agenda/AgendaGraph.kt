package com.example.tasky.android.agenda

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.tasky.android.agenda.screen.Agenda
import com.example.tasky.android.agenda.screen.AgendaDetailsScreenType
import com.example.tasky.android.agenda.screen.agendaDetailsScreen
import com.example.tasky.android.agenda.screen.agendaScreen
import com.example.tasky.android.agenda.screen.navigateToAgendaDetails
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import kotlinx.serialization.Serializable

@Serializable
object AgendaGraph

fun NavGraphBuilder.agendaGraph(navController: NavController) {
    navigation<AgendaGraph>(startDestination = Agenda) {
        agendaScreen(
            navigateToCreateEvent = { },
            navigateToCreateTask = { },
            navigateToCreateReminder = { },
            navigateToAgendaDetails = { item ->
                navController.navigateToAgendaDetails(
                    agendaId = item.id,
                    type =
                        when (item) {
                            is Event -> AgendaDetailsScreenType.EVENT
                            is Reminder -> AgendaDetailsScreenType.REMINDER
                            is Task -> AgendaDetailsScreenType.TASK
                        },
                )
            },
            navigateToEditAgenda = { item ->
                navController.navigateToAgendaDetails(
                    agendaId = item.id,
                    type =
                        when (item) {
                            is Event -> AgendaDetailsScreenType.EVENT
                            is Reminder -> AgendaDetailsScreenType.REMINDER
                            is Task -> AgendaDetailsScreenType.TASK
                        },
                    isEdit = true,
                )
            },
        )

        agendaDetailsScreen(
            navigateUp = navController::navigateUp,
        )
    }
}

fun NavController.navigateToAgendaGraph() {
    navigate(AgendaGraph)
}
