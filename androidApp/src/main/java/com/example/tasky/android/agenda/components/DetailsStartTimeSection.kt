package com.example.tasky.android.agenda.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun DetailsStartTimeSection(
    item: AgendaItem,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(prefixId(item = item)), style = typography.bodySmall, lineHeight = 15.sp, color = Black)
        Spacer(modifier = Modifier.width(40.dp))
        Text(formatTime(item.getStartTime()), style = typography.bodySmall, lineHeight = 15.sp, color = Black)
        Spacer(modifier = Modifier.width(80.dp))
        Text(
            formatDate(item.getStartTime()),
            style = typography.bodySmall,
            lineHeight = 15.sp,
            color = Black,
        )
    }
}

private fun prefixId(item: AgendaItem): Int =
    when (item) {
        is Event -> R.string.from
        is Task, is Reminder -> R.string.at
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

@Preview
@Composable
private fun DetailsStartTimeSectionPreview() {
    MyApplicationTheme {
        DetailsStartTimeSection(Task.DUMMY)
    }
}
