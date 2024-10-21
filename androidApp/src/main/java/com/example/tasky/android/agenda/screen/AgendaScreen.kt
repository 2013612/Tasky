package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tasky.android.R
import com.example.tasky.android.agenda.components.AgendaCard
import com.example.tasky.android.agenda.components.AgendaDayBar
import com.example.tasky.android.agenda.components.AgendaFloatingActionButton
import com.example.tasky.android.agenda.components.AgendaTopBar
import com.example.tasky.android.agenda.components.TimeNeedle
import com.example.tasky.android.agenda.viewmodel.AgendaViewModel
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import com.example.tasky.repository.AgendaRepository
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

fun NavGraphBuilder.agendaScreen(
    navigateToCreateEvent: () -> Unit,
    navigateToCreateTask: () -> Unit,
    navigateToCreateReminder: () -> Unit,
    navigateToAgendaDetails: (AgendaItem) -> Unit,
    navigateToEditAgenda: (AgendaItem) -> Unit,
) {
    composable<Agenda> {
        val viewModel: AgendaViewModel =
            viewModel {
                AgendaViewModel(AgendaRepository())
            }

        val screenState by viewModel.screenStateFlow.collectAsStateWithLifecycle()

        LaunchedEffect(true) {
            viewModel.initState()
        }

        AgendaScreen(screenState, onEvent = { event ->
            when (event) {
                AgendaScreenEvent.OnClickToCreateEvent -> navigateToCreateEvent()
                AgendaScreenEvent.OnClickToCreateReminder -> navigateToCreateReminder()
                AgendaScreenEvent.OnClickToCreateTask -> navigateToCreateTask()
                is AgendaScreenEvent.OnEditClick -> navigateToEditAgenda(event.agendaItem)
                is AgendaScreenEvent.OnOpenClick -> navigateToAgendaDetails(event.agendaItem)
                else -> viewModel.onEvent(event)
            }
        })
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

sealed interface AgendaScreenEvent {
    data object OnClickLogout : AgendaScreenEvent

    data object OnClickToCreateTask : AgendaScreenEvent

    data object OnClickToCreateEvent : AgendaScreenEvent

    data object OnClickToCreateReminder : AgendaScreenEvent

    data class OnDateSelect(
        val newDate: Long,
    ) : AgendaScreenEvent

    data class OnDayOffsetSelect(
        val newOffset: Int,
    ) : AgendaScreenEvent

    data class OnOpenClick(
        val agendaItem: AgendaItem,
    ) : AgendaScreenEvent

    data class OnEditClick(
        val agendaItem: AgendaItem,
    ) : AgendaScreenEvent

    data class OnDeleteClick(
        val agendaItem: AgendaItem,
    ) : AgendaScreenEvent
}

@Composable
private fun AgendaScreen(
    state: AgendaScreenState,
    onEvent: (AgendaScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            AgendaTopBar(
                date = state.startDate.toLocalDateTime(TimeZone.currentSystemDefault()),
                name = state.name,
                onLogoutClick = {
                    onEvent(AgendaScreenEvent.OnClickLogout)
                },
                onDateSelect = {
                    onEvent(AgendaScreenEvent.OnDateSelect(it))
                },
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
                    days =
                        (0..state.numberOfDateShown)
                            .map {
                                val now = state.startDate
                                now.plus(it, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                            }.toImmutableList(),
                    selectedDayOffset = state.selectedDateOffset,
                    onDaySelect = {
                        onEvent(AgendaScreenEvent.OnDayOffsetSelect(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.today),
                    style = typography.headlineMedium,
                    color = Black,
                    lineHeight = 16.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    val timeNeedleIndex = getTimeNeedleDisplayIndex(state.agendas)
                    if (timeNeedleIndex < 0) {
                        item {
                            TimeNeedle()
                        }
                    }
                    itemsIndexed(state.agendas) { index, item ->
                        AgendaCard(agendaItem = item, onOpenClick = {
                            onEvent(AgendaScreenEvent.OnOpenClick(item))
                        }, onDeleteClick = {
                            onEvent(AgendaScreenEvent.OnDeleteClick(item))
                        }, onEditClick = {
                            onEvent(AgendaScreenEvent.OnEditClick(item))
                        })
                        TimeNeedle(modifier = Modifier.alpha(if (timeNeedleIndex == index) 1f else 0f))
                    }
                }
            }
        }

        AgendaFloatingActionButton(
            onEventClick = {
                onEvent(AgendaScreenEvent.OnClickToCreateEvent)
            },
            onTaskClick = {
                onEvent(AgendaScreenEvent.OnClickToCreateTask)
            },
            onReminderClick = {
                onEvent(AgendaScreenEvent.OnClickToCreateReminder)
            },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
        )
    }
}

private fun getTimeNeedleDisplayIndex(agendaItemsSortedByStartTime: ImmutableList<AgendaItem>): Int {
    val currentTime = System.currentTimeMillis()
    val index = agendaItemsSortedByStartTime.indexOfFirst { it.getStartTime() > currentTime }

    return if (index < 0) {
        agendaItemsSortedByStartTime.lastIndex
    } else {
        index - 1
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    MyApplicationTheme {
        AgendaScreen(
            state =
                AgendaScreenState(
                    agendas = persistentListOf(Event.DUMMY, Task.DUMMY, Reminder.DUMMY),
                ),
            onEvent = {},
        )
    }
}
