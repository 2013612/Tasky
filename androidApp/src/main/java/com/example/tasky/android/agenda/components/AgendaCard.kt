package com.example.tasky.android.agenda.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Brown
import com.example.tasky.android.theme.DarkGray
import com.example.tasky.android.theme.Green
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightGreen
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task

@Composable
fun AgendaCard(
    agendaItem: AgendaItem,
    onOpenClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCircleClick: (() -> Unit)? = null,
) {
    var isMenuOpen by remember {
        mutableStateOf(false)
    }
    Card(
        colors = CardDefaults.cardColors().copy(containerColor = getBackgroundColor(agendaItem)),
        shape = shapes.medium,
        modifier = modifier,
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onCircleClick?.invoke()
                }, enabled = onCircleClick != null) {
                    Icon(
                        painter = painterResource(R.drawable.outline_circle_24),
                        contentDescription = null,
                        tint = getCheckIconColor(agendaItem),
                    )
                }
                Text("Project X", style = typography.headlineMedium, color = getTitleTextColor(agendaItem), modifier = Modifier.weight(1f))
                Box {
                    IconButton(onClick = {
                        isMenuOpen = true
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_more_horiz_24),
                            contentDescription = null,
                            tint = getMoreIconColor(agendaItem),
                        )
                    }
                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = { isMenuOpen = false },
                    ) {
                        DropdownMenuItem(text = {
                            Text("Open")
                        }, onClick = {
                            onOpenClick()
                            isMenuOpen = false
                        })
                        DropdownMenuItem(text = {
                            Text("Edit")
                        }, onClick = {
                            onEditClick()
                            isMenuOpen = false
                        })
                        DropdownMenuItem(text = {
                            Text("Delete")
                        }, onClick = {
                            onDeleteClick()
                            isMenuOpen = false
                        })
                    }
                }
            }
            Row {
                IconButton(onClick = {}, modifier = Modifier.alpha(0f)) {
                    Icon(painter = painterResource(R.drawable.outline_more_horiz_24), contentDescription = null)
                }
                Text("Just work", style = typography.labelMedium, color = getDescTextColor(agendaItem))
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text("Mar 5, 10:30 - Mar 5, 11:00", style = typography.labelMedium, color = getDescTextColor(agendaItem))
                Spacer(Modifier.width(8.dp))
            }
        }
    }
}

private fun getBackgroundColor(agendaItem: AgendaItem): Color =
    when (agendaItem) {
        is Event -> LightGreen
        is Task -> Green
        is Reminder -> Light2
    }

private fun getTitleTextColor(agendaItem: AgendaItem): Color =
    when (agendaItem) {
        is Task -> Color.White
        is Event, is Reminder -> Black
    }

private fun getDescTextColor(agendaItem: AgendaItem): Color =
    when (agendaItem) {
        is Task -> Color.White
        is Event, is Reminder -> DarkGray
    }

private fun getCheckIconColor(agendaItem: AgendaItem): Color =
    when (agendaItem) {
        is Task -> Color.White
        is Event, is Reminder -> Black
    }

private fun getMoreIconColor(agendaItem: AgendaItem): Color =
    when (agendaItem) {
        is Task -> Color.White
        is Event, is Reminder -> Brown
    }

private fun getTimeDisplay(agendaItem: AgendaItem): String {
    val dateTimeFormat =
        LocalDateTime.Format {
            monthName(names = MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth()
            char(',')
            char(' ')
            hour()
            char(':')
            minute()
        }
    return when (agendaItem) {
        is Task ->
            Instant
                .fromEpochSeconds(agendaItem.time)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .format(dateTimeFormat)

        is Reminder ->
            Instant
                .fromEpochSeconds(agendaItem.time)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .format(dateTimeFormat)

        is Event ->
            "${
                Instant.fromEpochSeconds(agendaItem.from)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).format(dateTimeFormat)
            } - ${
                Instant.fromEpochSeconds(agendaItem.to).toLocalDateTime(TimeZone.currentSystemDefault())
                    .format(dateTimeFormat)
            }"
    }
}

@Preview
@Composable
private fun AgendaCardPreview() {
    MyApplicationTheme {
        AgendaCard(Event(id = 1), {}, {}, {}, modifier = Modifier.height(123.dp))
    }
}
