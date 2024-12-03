package com.example.tasky.android.agenda.presentation.common.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tasky.android.R

@Composable
fun DeleteAgendaDialog(
    agendaTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(R.string.cancel),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    stringResource(R.string.delete),
                )
            }
        },
        title = {
            Text(text = stringResource(R.string.delete_dialog_title))
        },
        text = {
            Text(
                stringResource(R.string.delete_dialog_desc, agendaTitle),
            )
        },
        modifier = modifier,
    )
}
