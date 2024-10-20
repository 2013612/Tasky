package com.example.tasky.android.agenda.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
fun AgendaFloatingActionButton(
    onEventClick: () -> Unit,
    onTaskClick: () -> Unit,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuOpen by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = {
                isMenuOpen = true
            },
            containerColor = Black,
            shape = RoundedCornerShape(16.dp),
        ) {
            Icon(Icons.Outlined.Add, contentDescription = null, tint = Color.White)
        }

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false },
        ) {
            DropdownMenuItem(text = {
                Text(stringResource(R.string.event))
            }, onClick = {
                onEventClick()
                isMenuOpen = false
            })
            DropdownMenuItem(text = {
                Text(stringResource(R.string.task))
            }, onClick = {
                onTaskClick()
                isMenuOpen = false
            })
            DropdownMenuItem(text = {
                Text(stringResource(R.string.reminder))
            }, onClick = {
                onReminderClick()
                isMenuOpen = false
            })
        }
    }
}

@Preview
@Composable
private fun AgendaFloatingActionButtonPreview() {
    MyApplicationTheme {
        AgendaFloatingActionButton({}, {}, {})
    }
}
