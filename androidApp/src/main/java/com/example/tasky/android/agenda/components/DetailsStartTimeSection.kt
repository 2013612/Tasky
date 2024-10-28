package com.example.tasky.android.agenda.components

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
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsStartTimeSection(
    item: AgendaItem,
    isEdit: Boolean,
    onDateTimeSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDateDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isTimeDialogOpen by rememberSaveable { mutableStateOf(false) }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            stringResource(
                when (item) {
                    is Event -> R.string.from
                    is Task, is Reminder -> R.string.at
                },
            ),
            style = typography.bodySmall,
            lineHeight = 15.sp,
            color = Black,
        )
        Spacer(modifier = Modifier.width(40.dp))
        TextButton(onClick = {
            isTimeDialogOpen = true
        }) {
            Text(
                formatTime(item.getStartTime()),
                style = typography.bodySmall,
                lineHeight = 15.sp,
                color = Black,
            )
        }
        Spacer(modifier = Modifier.width(80.dp))
        TextButton(onClick = {
            isDateDialogOpen = true
        }, enabled = isEdit) {
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
                    val newTime = (timePickerState.hour * 60 + timePickerState.minute) * 60 * 1000
                    val newStartTime =
                        item.getStartTime() / (24 * 60 * 60 * 1000) * (24 * 60 * 60 * 1000) + newTime
                    onDateTimeSelect(newStartTime)
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
                        val originTime = item.getStartTime() % (24 * 60 * 60 * 1000)
                        datePickerState.selectedDateMillis?.let { onDateTimeSelect(it + originTime) }
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
        DetailsStartTimeSection(
            Task.DUMMY,
            isEdit = true,
            onDateTimeSelect = {},
            modifier = Modifier,
        )
    }
}
