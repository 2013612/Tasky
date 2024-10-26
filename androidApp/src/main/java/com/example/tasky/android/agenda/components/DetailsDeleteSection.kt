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
import java.util.Locale

@Composable
fun DetailsDeleteSection(
    item: AgendaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            "${stringResource(R.string.delete)} ${
                stringResource(
                    when (item) {
                        is Event -> R.string.event
                        is Task -> R.string.task
                        is Reminder -> R.string.reminder
                    },
                )
            }".uppercase(
                Locale.getDefault(),
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
        DetailsDeleteSection(Task.DUMMY, {})
    }
}
