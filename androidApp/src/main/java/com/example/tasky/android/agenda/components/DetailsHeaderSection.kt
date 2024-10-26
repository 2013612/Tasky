package com.example.tasky.android.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
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
fun DetailsHeaderSection(
    item: AgendaItem,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(20.dp).background(getColor(item = item)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            stringResource(getStringRes(item = item)),
            style = typography.bodyMedium,
            color = DarkGray,
            lineHeight = 19.2.sp,
        )
    }
}

private fun getColor(item: AgendaItem): Color =
    when (item) {
        is Event -> LightGreen
        is Reminder -> Light2
        is Task -> Green
    }

private fun getStringRes(item: AgendaItem): Int =
    when (item) {
        is Event -> R.string.event
        is Reminder -> R.string.reminder
        is Task -> R.string.task
    }

@Preview
@Composable
private fun DetailsHeaderSectionPreview() {
    MyApplicationTheme {
        DetailsHeaderSection(item = Task.DUMMY)
    }
}
