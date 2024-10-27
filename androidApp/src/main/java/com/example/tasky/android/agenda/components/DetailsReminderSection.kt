package com.example.tasky.android.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

enum class ReminderType(val milliSecond: Long) {
    TEN_MINUTE((10 * 60 * 1000).toLong()),
    THIRTY_MINUTE((30 * 60 * 1000).toLong()),
    ONE_HOUR((60 * 60 * 1000).toLong()),
    SIX_HOUR((6 * 60 * 60 * 1000).toLong()),
    ONE_DAY((24 * 60 * 60 * 1000).toLong()),
}

@Composable
fun DetailsReminderSection(
    item: AgendaItem,
    isEdit: Boolean,
    onReminderSelect: (ReminderType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
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
}

private fun getReminderTextId(time: Long): Int =
    when (time) {
        ReminderType.TEN_MINUTE.milliSecond -> R.string._10_minutes_before
        ReminderType.THIRTY_MINUTE.milliSecond -> R.string._30_minutes_before
        ReminderType.ONE_HOUR.milliSecond -> R.string._1_hour_before
        ReminderType.SIX_HOUR.milliSecond -> R.string._6_hours_before
        ReminderType.ONE_DAY.milliSecond -> R.string._1_day_before
        else -> R.string._10_minutes_before
    }
