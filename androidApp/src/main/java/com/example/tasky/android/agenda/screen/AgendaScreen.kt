package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tasky.android.agenda.components.AgendaDayBar
import com.example.tasky.android.agenda.components.AgendaTopBar
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

fun NavGraphBuilder.agendaScreen() {
    composable<Agenda> {
        AgendaScreen(AgendaScreenState())
    }
}

fun NavController.navigateToAgenda() {
    navigate(Agenda)
}

@Serializable
object Agenda

data class AgendaScreenState(
    val agendas: ImmutableList<AgendaItem> = persistentListOf(),
    val name: String = "",
    val startDate: Instant = Clock.System.now(),
    val numberOfDateShown: Int = 6,
    val selectedDateOffset: Int = 0,
)

@Composable
private fun AgendaScreen(
    state: AgendaScreenState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AgendaTopBar(
            month = state.startDate.toLocalDateTime(TimeZone.currentSystemDefault()).month,
            name = state.name,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    ).padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            AgendaDayBar(
                (0..state.numberOfDateShown)
                    .map {
                        val now = state.startDate
                        now.plus(it, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                    }.toImmutableList(),
                state.selectedDateOffset,
                {},
                Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    MyApplicationTheme {
        AgendaScreen(state = AgendaScreenState())
    }
}
