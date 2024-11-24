package com.example.tasky.android.agenda.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.agenda.presentation.screen.Agenda
import com.example.tasky.android.agenda.presentation.screen.agendaDetailsScreen
import com.example.tasky.android.agenda.presentation.screen.agendaScreen
import com.example.tasky.android.agenda.presentation.screen.navigateToAgendaDetails
import kotlinx.serialization.Serializable

@Serializable
object AgendaGraph

fun NavGraphBuilder.agendaGraph(navController: NavController) {
    navigation<AgendaGraph>(startDestination = Agenda) {
        agendaScreen(
            navigateToCreateAgenda = { id, type ->
                navController.navigateToAgendaDetails(
                    agendaId = id,
                    type = type,
                    isEdit = true,
                )
            },
            navigateToAgendaDetails = { item ->
                navController.navigateToAgendaDetails(
                    agendaId = item.id,
                    type =
                        when (item) {
                            is Event -> AgendaType.EVENT
                            is Reminder -> AgendaType.REMINDER
                            is Task -> AgendaType.TASK
                        },
                )
            },
            navigateToEditAgenda = { item ->
                navController.navigateToAgendaDetails(
                    agendaId = item.id,
                    type =
                        when (item) {
                            is Event -> AgendaType.EVENT
                            is Reminder -> AgendaType.REMINDER
                            is Task -> AgendaType.TASK
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
