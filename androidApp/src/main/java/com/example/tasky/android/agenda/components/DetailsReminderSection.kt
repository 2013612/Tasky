package com.example.tasky.android.agenda.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.Light2
import com.example.tasky.model.agenda.AgendaItem

enum class ReminderType(
    val milliSecond: Long,
    @StringRes val stringId: Int,
) {
    TEN_MINUTE((10 * 60 * 1000).toLong(), R.string._10_minutes_before),
    THIRTY_MINUTE((30 * 60 * 1000).toLong(), R.string._30_minutes_before),
    ONE_HOUR((60 * 60 * 1000).toLong(), R.string._1_hour_before),
    SIX_HOUR((6 * 60 * 60 * 1000).toLong(), R.string._6_hours_before),
    ONE_DAY((24 * 60 * 60 * 1000).toLong(), R.string._1_day_before),
}

@Composable
fun DetailsReminderSection(
    item: AgendaItem,
    isEdit: Boolean,
    onReminderSelect: (ReminderType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuOpen by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        Row(modifier = Modifier.clickable(enabled = isEdit) { isMenuOpen = true }, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(30.dp)
                        .background(Light2),
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Gray,
                    modifier =
                        Modifier.align(
                            Alignment.Center,
                        ),
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(getReminderTextId(item.getStartTime() - item.remindAt)),
                style = typography.bodySmall,
                lineHeight = 15.sp,
                color = Black,
                modifier = Modifier.weight(1f),
            )

            if (isEdit) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = isMenuOpen,
            onDismissRequest = { isMenuOpen = false },
        ) {
            ReminderType.entries.map {
                DropdownMenuItem(text = {
                    Text(stringResource(it.stringId))
                }, onClick = {
                    onReminderSelect(it)
                    isMenuOpen = false
                })
            }
        }
    }
}

private fun getReminderTextId(time: Long): Int =
    ReminderType.entries.firstOrNull { it.milliSecond == time }?.stringId ?: R.string._10_minutes_before
