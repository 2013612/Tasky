package com.example.tasky.android.agenda.presentation.details.component

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.R
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.MyApplicationTheme

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
                when (item) {
                    is Event ->
                        when {
                            item.isUserEventCreator -> R.string.delete_event
                            eventIsGoing -> R.string.leave_event
                            else -> R.string.join_event
                        }
                    is Task -> R.string.delete_task
                    is Reminder -> R.string.delete_reminder
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
