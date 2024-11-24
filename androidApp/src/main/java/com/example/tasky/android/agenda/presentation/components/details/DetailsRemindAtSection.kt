package com.example.tasky.android.agenda.presentation.components.details

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.agenda.domain.model.RemindAtType
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.R
import com.example.tasky.android.common.presentation.utils.UiText
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
fun DetailsRemindAtSection(
    item: AgendaItem,
    isEdit: Boolean,
    onRemindAtSelect: (RemindAtType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier.clickable(enabled = isEdit) { isMenuOpen = true },
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                getRemindAtText(item.remindAt).asString(),
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
            RemindAtType.entries.map {
                DropdownMenuItem(text = {
                    Text(getRemindAtText(it).asString())
                }, onClick = {
                    onRemindAtSelect(it)
                    isMenuOpen = false
                })
            }
        }
    }
}

private fun getRemindAtText(type: RemindAtType): UiText =
    when (type) {
        RemindAtType.TEN_MINUTE -> UiText.StringResource(R.string._10_minutes_before)
        RemindAtType.THIRTY_MINUTE -> UiText.StringResource(R.string._30_minutes_before)
        RemindAtType.ONE_HOUR -> UiText.StringResource(R.string._1_hour_before)
        RemindAtType.SIX_HOUR -> UiText.StringResource(R.string._6_hours_before)
        RemindAtType.ONE_DAY -> UiText.StringResource(R.string._1_day_before)
    }

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailsRemindAtSectionPreview() {
    MyApplicationTheme {
        DetailsRemindAtSection(item = Task.DUMMY, isEdit = false, onRemindAtSelect = {})
    }
}
