package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.R
import com.example.tasky.android.agenda.components.agenda.AgendaCard
import com.example.tasky.android.agenda.components.agenda.AgendaDayBar
import com.example.tasky.android.agenda.components.agenda.AgendaFloatingActionButton
import com.example.tasky.android.agenda.components.agenda.AgendaTimeNeedle
import com.example.tasky.android.agenda.components.agenda.AgendaTopBar
import com.example.tasky.android.agenda.viewmodel.AgendaOneTimeEvent
import com.example.tasky.android.agenda.viewmodel.AgendaViewModel
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.android.utils.ObserveAsEvents
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
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.agendaScreen(
    navigateToCreateAgenda: (String, AgendaDetailsScreenType) -> Unit,
    navigateToAgendaDetails: (AgendaItem) -> Unit,
    navigateToEditAgenda: (AgendaItem) -> Unit,
) {
    composable<Agenda> {
        val viewModel: AgendaViewModel = koinViewModel()

        val screenState by viewModel.screenStateFlow.collectAsStateWithLifecycle()

        ObserveAsEvents(viewModel.eventsFlow) { event ->
            when (event) {
                is AgendaOneTimeEvent.OnAgendaCreate -> {
                    navigateToCreateAgenda(event.id, event.type)
                }
            }
        }

        LaunchedEffect(true) {
            viewModel.initState()
        }

        AgendaScreen(screenState, onEvent = { event ->
            when (event) {
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
    val agendas: ImmutableList<AgendaItemUi> = persistentListOf(),
    val name: String = "",
    val startDate: Instant = Clock.System.now(),
    val numberOfDateShown: Int = 6,
    val selectedDateOffset: Int = 0,
)

sealed interface AgendaScreenEvent {
    data object OnClickLogout : AgendaScreenEvent

    data class OnCreateClick(
        val type: AgendaDetailsScreenType,
    ) : AgendaScreenEvent

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

    data class OnAgendaCircleClick(
        val task: Task,
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
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.agendas, key = {
                        when (it) {
                            is AgendaItemUi.Item -> it.item.id
                            AgendaItemUi.Needle -> "needle"
                        }
                    }) { itemUi ->
                        when (itemUi) {
                            is AgendaItemUi.Item ->
                                AgendaCard(
                                    agendaItem = itemUi.item,
                                    onOpenClick = {
                                        onEvent(AgendaScreenEvent.OnOpenClick(itemUi.item))
                                    },
                                    onDeleteClick = {
                                        onEvent(AgendaScreenEvent.OnDeleteClick(itemUi.item))
                                    },
                                    onEditClick = {
                                        onEvent(AgendaScreenEvent.OnEditClick(itemUi.item))
                                    },
                                    onCircleClick =
                                        if (itemUi.item is Task) {
                                            { onEvent(AgendaScreenEvent.OnAgendaCircleClick(task = itemUi.item)) }
                                        } else {
                                            null
                                        },
                                )

                            AgendaItemUi.Needle -> AgendaTimeNeedle()
                        }
                    }
                }
            }
        }

        AgendaFloatingActionButton(
            onEventClick = {
                onEvent(AgendaScreenEvent.OnCreateClick(type = AgendaDetailsScreenType.EVENT))
            },
            onTaskClick = {
                onEvent(AgendaScreenEvent.OnCreateClick(type = AgendaDetailsScreenType.TASK))
            },
            onReminderClick = {
                onEvent(AgendaScreenEvent.OnCreateClick(type = AgendaDetailsScreenType.REMINDER))
            },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
        )
    }
}

sealed interface AgendaItemUi {
    data class Item(
        val item: AgendaItem,
    ) : AgendaItemUi

    data object Needle : AgendaItemUi
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    MyApplicationTheme {
        AgendaScreen(
            state =
                AgendaScreenState(
                    agendas =
                        persistentListOf(
                            AgendaItemUi.Item(Event.DUMMY),
                            AgendaItemUi.Item(Task.DUMMY),
                            AgendaItemUi.Needle,
                            AgendaItemUi.Item(Reminder.DUMMY),
                        ),
                ),
            onEvent = {},
        )
    }
}
