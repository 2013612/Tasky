package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.tasky.android.R
import com.example.tasky.android.agenda.components.AgendaEditText
import com.example.tasky.android.agenda.components.AgendaEditTextType
import com.example.tasky.android.agenda.components.DetailsDeleteSection
import com.example.tasky.android.agenda.components.DetailsDescSection
import com.example.tasky.android.agenda.components.DetailsHeaderSection
import com.example.tasky.android.agenda.components.DetailsRemindAtSection
import com.example.tasky.android.agenda.components.DetailsStartTimeSection
import com.example.tasky.android.agenda.components.DetailsTitleSection
import com.example.tasky.android.agenda.components.DetailsTopBar
import com.example.tasky.android.agenda.viewmodel.AgendaDetailsViewModel
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.android.utils.serializableNavType
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.format.DateTimeFormatter
import kotlin.reflect.typeOf
import kotlin.time.DurationUnit

enum class AgendaDetailsScreenType {
    TASK,
    EVENT,
    REMINDER,
}

fun NavGraphBuilder.agendaDetailsScreen(navigateUp: () -> Unit) {
    composable<AgendaDetails>(typeMap = AgendaDetails.typeMap) {
        val args = it.toRoute<AgendaDetails>()
        val viewModel: AgendaDetailsViewModel =
            koinViewModel {
                parametersOf(args.agendaId, args.type)
            }

        val screenState by viewModel.screenStateFlow.collectAsStateWithLifecycle()

        val isDeleteSuccess by viewModel.isDeleteSuccess.collectAsStateWithLifecycle()

        LaunchedEffect(isDeleteSuccess) {
            if (isDeleteSuccess) {
                navigateUp()
            }
        }

        AgendaDetailsScreen(
            state = screenState,
            onEvent = { event ->
                when (event) {
                    AgendaDetailsScreenEvent.OnCloseClick -> navigateUp()
                    else -> viewModel.onEvent(event)
                }
            },
        )
    }
}

fun NavController.navigateToAgendaDetails(
    agendaId: String,
    type: AgendaDetailsScreenType,
) {
    navigate(AgendaDetails(agendaId = agendaId, type = type))
}

@Serializable
private data class AgendaDetails(
    val agendaId: String,
    val type: AgendaDetailsScreenType,
) {
    companion object {
        val typeMap = mapOf(typeOf<AgendaDetailsScreenType>() to serializableNavType<AgendaDetailsScreenType>())
    }
}

data class AgendaDetailsScreenState(
    val agendaItem: AgendaItem,
    val isEdit: Boolean = false,
    val agendaEditTextType: AgendaEditTextType? = null,
)

sealed interface AgendaDetailsScreenEvent {
    data object OnCloseClick : AgendaDetailsScreenEvent

    data object OnSaveClick : AgendaDetailsScreenEvent

    data object OnEditClick : AgendaDetailsScreenEvent

    data object OnTitleClick : AgendaDetailsScreenEvent

    data object OnDescClick : AgendaDetailsScreenEvent

    data object OnDeleteClick : AgendaDetailsScreenEvent

    data object CloseEditText : AgendaDetailsScreenEvent

    data class OnTitleChange(
        val newTitle: String,
    ) : AgendaDetailsScreenEvent

    data class OnDescChange(
        val newDesc: String,
    ) : AgendaDetailsScreenEvent

    data class OnStartTimeChange(
        val newHour: Int,
        val newMinute: Int,
    ) : AgendaDetailsScreenEvent

    data class OnStartDateChange(
        val newDate: Long,
    ) : AgendaDetailsScreenEvent

    data class OnRemindAtChange(
        val newRemindAtTime: Long,
    ) : AgendaDetailsScreenEvent
}

@Composable
private fun AgendaDetailsScreen(
    state: AgendaDetailsScreenState,
    onEvent: (AgendaDetailsScreenEvent) -> Unit,
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
            DetailsTopBar(
                title =
                    if (state.isEdit) {
                        stringResource(
                            when (state.agendaItem) {
                                is Task -> R.string.edit_task
                                is Event -> R.string.edit_event
                                is Reminder -> R.string.edit_reminder
                            },
                        )
                    } else {
                        getTitleTimeDisplay(state.agendaItem.getStartTime())
                    },
                isEdit = state.isEdit,
                onCloseClick = {
                    onEvent(AgendaDetailsScreenEvent.OnCloseClick)
                },
                onEditClick = {
                    onEvent(AgendaDetailsScreenEvent.OnEditClick)
                },
                onSaveClick = {
                    onEvent(AgendaDetailsScreenEvent.OnSaveClick)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        ).padding(top = 30.dp, start = 16.dp, end = 16.dp)
                        .verticalScroll(rememberScrollState()),
            ) {
                DetailsHeaderSection(item = state.agendaItem)
                Spacer(Modifier.height(30.dp))

                DetailsTitleSection(
                    title = state.agendaItem.title,
                    isEdit = state.isEdit,
                    modifier =
                        Modifier.clickable(enabled = state.isEdit) {
                            onEvent(AgendaDetailsScreenEvent.OnTitleClick)
                        },
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsDescSection(
                    desc = state.agendaItem.description,
                    isEdit = state.isEdit,
                    modifier =
                        Modifier.clickable(enabled = state.isEdit) {
                            onEvent(AgendaDetailsScreenEvent.OnDescClick)
                        },
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsStartTimeSection(
                    item = state.agendaItem,
                    isEdit = state.isEdit,
                    onDateSelect = {
                        onEvent(AgendaDetailsScreenEvent.OnStartDateChange(it))
                    },
                    onTimeSelect = { hour, minute ->
                        onEvent(
                            AgendaDetailsScreenEvent.OnStartTimeChange(
                                newHour = hour,
                                newMinute = minute,
                            ),
                        )
                    },
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsRemindAtSection(
                    item = state.agendaItem,
                    isEdit = state.isEdit,
                    onRemindAtSelect = {
                        onEvent(
                            AgendaDetailsScreenEvent.OnRemindAtChange(
                                it.duration.toLong(
                                    DurationUnit.MILLISECONDS,
                                ),
                            ),
                        )
                    },
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)

                Spacer(Modifier.weight(1f))

                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))
                DetailsDeleteSection(
                    item = state.agendaItem,
                    onClick = {
                        onEvent(AgendaDetailsScreenEvent.OnDeleteClick)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(32.dp))
            }
        }

        if (state.agendaEditTextType != null) {
            AgendaEditText(
                initialValue =
                    when (state.agendaEditTextType) {
                        AgendaEditTextType.TITLE -> state.agendaItem.title
                        AgendaEditTextType.DESCRIPTION -> state.agendaItem.description
                    },
                type = state.agendaEditTextType,
                onBackClick = {
                    onEvent(AgendaDetailsScreenEvent.CloseEditText)
                },
                onSaveClick = {
                    when (state.agendaEditTextType) {
                        AgendaEditTextType.TITLE ->
                            onEvent(
                                AgendaDetailsScreenEvent.OnTitleChange(
                                    it,
                                ),
                            )

                        AgendaEditTextType.DESCRIPTION ->
                            onEvent(
                                AgendaDetailsScreenEvent.OnDescChange(
                                    it,
                                ),
                            )
                    }
                    onEvent(AgendaDetailsScreenEvent.CloseEditText)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun getTitleTimeDisplay(time: Long): String {
    val dateTimeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

    return Instant
        .fromEpochMilliseconds(time)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toJavaLocalDateTime()
        .format(dateTimeFormat)
}

@Preview
@Composable
private fun AgendaDetailsScreenPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, false), onEvent = {})
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEditPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, true), onEvent = {})
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEditTextPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(
            state =
                AgendaDetailsScreenState(
                    Task.DUMMY,
                    true,
                    AgendaEditTextType.TITLE,
                ),
            onEvent = {},
        )
    }
}
