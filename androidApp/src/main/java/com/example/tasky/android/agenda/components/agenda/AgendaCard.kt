package com.example.tasky.android.agenda.components.agenda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda.data.model.AgendaItem
import com.example.tasky.agenda.data.model.Event
import com.example.tasky.agenda.data.model.Reminder
import com.example.tasky.agenda.data.model.Task
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Brown
import com.example.tasky.android.theme.DarkGray
import com.example.tasky.android.theme.Green
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightGreen
import com.example.tasky.android.theme.MyApplicationTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

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
                    if (agendaItem is Task && agendaItem.isDone) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = getCheckIconColor(agendaItem),
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.outline_circle_24),
                            contentDescription = null,
                            tint = getCheckIconColor(agendaItem),
                        )
                    }
                }
                Text(
                    agendaItem.title,
                    style = typography.headlineMedium,
                    color = getTitleTextColor(agendaItem),
                    textDecoration = if (agendaItem is Task && agendaItem.isDone) TextDecoration.LineThrough else null,
                    modifier = Modifier.weight(1f),
                )
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
                            Text(stringResource(R.string.open))
                        }, onClick = {
                            onOpenClick()
                            isMenuOpen = false
                        })
                        DropdownMenuItem(text = {
                            Text(stringResource(R.string.edit))
                        }, onClick = {
                            onEditClick()
                            isMenuOpen = false
                        })
                        DropdownMenuItem(text = {
                            Text(stringResource(R.string.delete))
                        }, onClick = {
                            onDeleteClick()
                            isMenuOpen = false
                        })
                    }
                }
            }
            Row {
                IconButton(onClick = {}, modifier = Modifier.alpha(0f)) {
                    Icon(
                        painter = painterResource(R.drawable.outline_more_horiz_24),
                        contentDescription = null,
                    )
                }
                Text(
                    agendaItem.description,
                    style = typography.labelMedium,
                    color = getDescTextColor(agendaItem),
                )
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    getTimeDisplay(agendaItem),
                    style = typography.labelMedium,
                    color = getDescTextColor(agendaItem),
                )
                Spacer(Modifier.width(8.dp))
            }
            Spacer(Modifier.height(16.dp))
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
    val dateTimeFormat = DateTimeFormatter.ofPattern("MMM d, HH:mm")
    val startTimeString =
        Instant
            .fromEpochMilliseconds(agendaItem.getStartTime())
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()
            .format(dateTimeFormat)

    return when (agendaItem) {
        is Task, is Reminder -> startTimeString

        is Event -> {
            val toDateTime =
                Instant
                    .fromEpochMilliseconds(agendaItem.to)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .toJavaLocalDateTime()
                    .format(dateTimeFormat)

            "$startTimeString - $toDateTime"
        }
    }
}

@Preview
@Composable
private fun AgendaCardPreview() {
    MyApplicationTheme {
        AgendaCard(Event.DUMMY, {}, {}, {}, modifier = Modifier.height(123.dp))
    }
}
