package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.agenda.components.DetailsDescSection
import com.example.tasky.android.agenda.components.DetailsHeaderSection
import com.example.tasky.android.agenda.components.DetailsStartTimeSection
import com.example.tasky.android.agenda.components.DetailsTitleSection
import com.example.tasky.android.agenda.components.DetailsTopBar
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import com.example.tasky.model.agenda.Task

data class DetailsScreenState(
    val agendaItem: AgendaItem,
    val isEdit: Boolean,
)

@Composable
private fun TaskScreen(
    state: DetailsScreenState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            DetailsTopBar(
                title = "01 MARCH 2022",
                isEdit = true,
                onCloseClick = {},
                onEditClick = {},
                onSaveClick = {},
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        ).padding(top = 30.dp, start = 16.dp, end = 16.dp)
                        .verticalScroll(rememberScrollState()),
            ) {
                DetailsHeaderSection(item = state.agendaItem)
                Spacer(Modifier.height(30.dp))

                DetailsTitleSection(title = state.agendaItem.title, isEdit = state.isEdit)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsDescSection(desc = state.agendaItem.description, isEdit = state.isEdit)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                DetailsStartTimeSection(state.agendaItem)

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(30.dp).background(Light2)) {
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
                        "30 minutes before",
                        style = typography.bodySmall,
                        lineHeight = 15.sp,
                        color = Black,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)

                Spacer(Modifier.weight(1f))

                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = {}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        "DELETE TASK",
                        style = typography.bodyMedium,
                        lineHeight = 30.sp,
                        color = Gray,
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
private fun TaskScreenPreview() {
    MyApplicationTheme {
        TaskScreen(state = DetailsScreenState(Task.DUMMY, false))
    }
}
