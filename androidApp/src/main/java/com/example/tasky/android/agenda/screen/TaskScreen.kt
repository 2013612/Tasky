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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.agenda.components.DetailsTopBar
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.DarkGray
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.Green
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
private fun TaskScreen(modifier: Modifier = Modifier) {
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
                Row {
                    Box(modifier = Modifier.size(20.dp).background(Green))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.task),
                        style = typography.bodyMedium,
                        color = DarkGray,
                        lineHeight = 19.2.sp,
                    )
                }
                Spacer(Modifier.height(30.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.outline_circle_24),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Project X",
                        style = typography.displaySmall,
                        lineHeight = 25.sp,
                        color = Black,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
                }
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Weekly plan\n" +
                            "Role distribution",
                        style = typography.bodySmall,
                        lineHeight = 15.sp,
                        color = Black,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
                }
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Light)
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("At", style = typography.bodySmall, lineHeight = 15.sp, color = Black)
                    Spacer(modifier = Modifier.width(40.dp))
                    Text("08:00", style = typography.bodySmall, lineHeight = 15.sp, color = Black)
                    Spacer(modifier = Modifier.width(80.dp))
                    Text(
                        "Jul 21 2022",
                        style = typography.bodySmall,
                        lineHeight = 15.sp,
                        color = Black,
                    )
                }

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
        TaskScreen()
    }
}
