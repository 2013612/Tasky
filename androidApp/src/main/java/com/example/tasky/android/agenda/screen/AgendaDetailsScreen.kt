package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.android.R
import com.example.tasky.android.agenda.components.AgendaEditText
import com.example.tasky.android.agenda.components.AgendaEditTextType
import com.example.tasky.android.agenda.components.DetailsDeleteSection
import com.example.tasky.android.agenda.components.DetailsDescSection
import com.example.tasky.android.agenda.components.DetailsHeaderSection
import com.example.tasky.android.agenda.components.DetailsReminderSection
import com.example.tasky.android.agenda.components.DetailsStartTimeSection
import com.example.tasky.android.agenda.components.DetailsTitleSection
import com.example.tasky.android.agenda.components.DetailsTopBar
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

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

    data class OnEditTextTypeChange(
        val newType: AgendaEditTextType?,
    ) : AgendaDetailsScreenEvent

    data class OnTitleChange(
        val newTitle: String,
    ) : AgendaDetailsScreenEvent

    data class OnDescChange(
        val newDesc: String,
    ) : AgendaDetailsScreenEvent

    data class OnStartDateTimeChange(
        val newDateTime: Long,
    ) : AgendaDetailsScreenEvent

    data class OnReminderChange(
        val newReminderTime: Long,
    ) : AgendaDetailsScreenEvent
}

@Composable
private fun AgendaDetailsScreen(
    state: AgendaDetailsScreenState,
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
                onCloseClick = {},
                onEditClick = {},
                onSaveClick = {},
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

                DetailsTitleSection(title = state.agendaItem.title, isEdit = state.isEdit)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsDescSection(desc = state.agendaItem.description, isEdit = state.isEdit)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsStartTimeSection(item = state.agendaItem, isEdit = state.isEdit, onDateTimeSelect = {})

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsReminderSection(item = state.agendaItem, isEdit = state.isEdit)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)

                Spacer(Modifier.weight(1f))

                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))
                DetailsDeleteSection(
                    item = state.agendaItem,
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(32.dp))
            }
        }

        if (state.agendaEditTextType != null) {
            AgendaEditText(
                initialValue = "",
                type = state.agendaEditTextType,
                onBackClick = {},
                onSaveClick = {},
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
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, false))
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEditPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, true))
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEditTextPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, true, AgendaEditTextType.TITLE))
    }
}
