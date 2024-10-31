package com.example.tasky.android.agenda.components

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Event
import com.example.tasky.model.agenda.Reminder
import com.example.tasky.model.agenda.Task

@Composable
fun DetailsDeleteSection(
    item: AgendaItem,
    eventIsGoing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            stringResource(
                if (item !is Event || item.isUserEventCreator) {
                    when (item) {
                        is Event -> R.string.delete_event
                        is Task -> R.string.delete_task
                        is Reminder -> R.string.delete_reminder
                    }
                } else if (eventIsGoing) {
                    R.string.leave_event
                } else {
                    R.string.join_event
                },
            ),
            style = typography.bodyMedium,
            lineHeight = 30.sp,
            color = Gray,
        )
    }
}

@Preview
@Composable
private fun DetailsDeleteSectionPreview() {
    MyApplicationTheme {
        DetailsDeleteSection(Task.DUMMY, true, {})
    }
}
