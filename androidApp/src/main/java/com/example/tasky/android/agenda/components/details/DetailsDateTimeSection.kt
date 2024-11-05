package com.example.tasky.android.agenda.components.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.agenda.data.model.AgendaItem
import com.example.tasky.agenda.data.model.Event
import com.example.tasky.agenda.data.model.Reminder
import com.example.tasky.agenda.data.model.Task
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.util.toLocalDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsDateTimeSection(
    item: AgendaItem,
    isEdit: Boolean,
    onTimeSelect: (hour: Int, minute: Int) -> Unit,
    onDateSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    isEventEndSection: Boolean = false,
) {
    var isDateDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isTimeDialogOpen by rememberSaveable { mutableStateOf(false) }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            stringResource(
                when (item) {
                    is Event -> if (isEventEndSection) R.string.to else R.string.from
                    is Task, is Reminder -> R.string.at
                },
            ),
            style = typography.bodySmall,
            lineHeight = 15.sp,
            color = Black,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = {
            isTimeDialogOpen = true
        }, modifier = Modifier.weight(3f)) {
            Text(
                formatTime(item.getStartTime()),
                style = typography.bodySmall,
                lineHeight = 15.sp,
                color = Black,
            )
        }
        TextButton(onClick = {
            isDateDialogOpen = true
        }, enabled = isEdit, modifier = Modifier.weight(4f)) {
            Text(
                formatDate(item.getStartTime()),
                style = typography.bodySmall,
                lineHeight = 15.sp,
                color = Black,
            )
        }
    }

    if (isTimeDialogOpen) {
        val hour = item.getStartTime() / (60 * 60 * 1000) % 24
        val minute = item.getStartTime() / (60 * 1000) % 60
        val timePickerState =
            rememberTimePickerState(
                initialHour = hour.toInt(),
                initialMinute = minute.toInt(),
                is24Hour = true,
            )

        AlertDialog(
            onDismissRequest = {
                isTimeDialogOpen = false
            },
            dismissButton = {
                TextButton(onClick = { isTimeDialogOpen = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelect(timePickerState.hour, timePickerState.minute)
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                )
            },
        )
    }

    if (isDateDialogOpen) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = item.getStartTime(),
            )
        val confirmEnabled =
            remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }

        DatePickerDialog(
            onDismissRequest = {
                isDateDialogOpen = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelect(it.toLocalDateTime().date) }
                        isDateDialogOpen = false
                    },
                    enabled = confirmEnabled.value,
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDateDialogOpen = false
                }) { Text(stringResource(R.string.cancel)) }
            },
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState()),
            )
        }
    }
}

private fun formatTime(time: Long): String {
    val dateTimeFormat = DateTimeFormatter.ofPattern("hh:mm")

    return Instant
        .fromEpochMilliseconds(time)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toJavaLocalDateTime()
        .format(dateTimeFormat)
}

private fun formatDate(time: Long): String {
    val dateTimeFormat = DateTimeFormatter.ofPattern("MMM dd yyyy")

    return Instant
        .fromEpochMilliseconds(time)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toJavaLocalDateTime()
        .format(dateTimeFormat)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailsStartTimeSectionPreview() {
    MyApplicationTheme {
        DetailsDateTimeSection(
            Task.DUMMY,
            isEdit = true,
            onDateSelect = {},
            onTimeSelect = { _, _ -> },
            modifier = Modifier,
        )
    }
}
