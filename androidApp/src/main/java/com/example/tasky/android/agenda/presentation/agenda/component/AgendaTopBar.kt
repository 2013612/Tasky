package com.example.tasky.android.agenda.presentation.agenda.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaTopBar(
    date: LocalDateTime,
    name: String,
    onLogoutClick: () -> Unit,
    onDateSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDateDialogOpen by remember { mutableStateOf(false) }
    var isMenuOpen by remember { mutableStateOf(false) }

    Row(
        modifier =
        modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier.clickable {
                    isDateDialogOpen = true
                },
        ) {
            Text(
                date.month.name,
                style = typography.bodyLarge,
                lineHeight = 19.2.sp,
                color = Color.White,
            )
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.White)
        }

        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .background(Light, CircleShape)
                    .clickable {
                        isMenuOpen = true
                    },
        ) {
            Text(
                name,
                style = typography.bodyMedium,
                fontSize = 13.sp,
                lineHeight = 15.6.sp,
                color = LightBlue,
                modifier =
                    Modifier.align(
                        Alignment.Center,
                    ),
            )

            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false },
            ) {
                DropdownMenuItem(text = {
                    Text(stringResource(R.string.logout))
                }, onClick = {
                    onLogoutClick()
                    isMenuOpen = false
                })
            }
        }
    }

    if (isDateDialogOpen) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    date
                        .toInstant(TimeZone.UTC)
                        .toEpochMilliseconds() +
                        java.util.TimeZone
                            .getDefault()
                            .rawOffset,
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
                        datePickerState.selectedDateMillis?.let { onDateSelect(it) }
                        isDateDialogOpen = false
                    },
                    enabled = confirmEnabled.value,
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { isDateDialogOpen = false }) { Text(stringResource(R.string.cancel)) }
            },
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Preview
@Composable
private fun AgendaTopBarPreview() {
    MyApplicationTheme {
        AgendaTopBar(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            "PL",
            {},
            {},
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }
}
