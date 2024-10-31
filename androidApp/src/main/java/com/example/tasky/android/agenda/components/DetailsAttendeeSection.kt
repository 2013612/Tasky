package com.example.tasky.android.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.Attendee
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun DetailsAttendeeSection(
    attendees: ImmutableList<Attendee>,
    curTab: DetailsAttendeeSectionTabOption,
    isEdit: Boolean,
    onTabSelect: (DetailsAttendeeSectionTabOption) -> Unit,
    onAddClick: (String) -> Unit,
    onDeleteIconClick: (String) -> Unit,
    creatorId: String,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.visitors), style = typography.headlineMedium, lineHeight = 16.sp, color = Black)
                if (isEdit) {
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = { showDialog = true },
                        modifier =
                            Modifier
                                .background(Light2)
                                .size(35.dp),
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null, tint = LightBlue)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            DetailsAttendeeSectionTab(curTab = curTab, onTabSelect = onTabSelect)

            if (curTab == DetailsAttendeeSectionTabOption.ALL || curTab == DetailsAttendeeSectionTabOption.GOING) {
                Spacer(modifier = Modifier.height(16.dp))
                DetailsAttendeeSectionAttendeeList(
                    titleId = R.string.going,
                    attendees =
                        attendees
                            .filter {
                                it.isGoing
                            }.toImmutableList(),
                    onDeleteIconClick = onDeleteIconClick,
                    isEdit = isEdit,
                    creatorId = creatorId,
                )
            }

            if (curTab == DetailsAttendeeSectionTabOption.ALL || curTab == DetailsAttendeeSectionTabOption.NOT_GOING) {
                Spacer(modifier = Modifier.height(16.dp))
                DetailsAttendeeSectionAttendeeList(
                    titleId = R.string.not_going,
                    attendees =
                        attendees
                            .filter {
                                !it.isGoing
                            }.toImmutableList(),
                    onDeleteIconClick = onDeleteIconClick,
                    isEdit = isEdit,
                    creatorId = creatorId,
                )
            }
        }

        if (showDialog) {
            var text by rememberSaveable {
                mutableStateOf("")
            }

            Dialog(onDismissRequest = { showDialog = false }) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                ) {
                    IconButton(onClick = { showDialog = false }, modifier = Modifier.align(Alignment.End)) {
                        Icon(Icons.Outlined.Close, contentDescription = null, tint = Black)
                    }
                    Column(
                        modifier =
                            Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 32.dp),
                    ) {
                        Text(
                            stringResource(R.string.add_visitor),
                            style = typography.headlineMedium,
                            lineHeight = 24.sp,
                            color = Black,
                            modifier =
                                Modifier.align(
                                    Alignment.CenterHorizontally,
                                ),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            text,
                            {
                                text = it
                            },
                            placeholder = {
                                Text(
                                    stringResource(R.string.email_address),
                                    style = typography.bodySmall,
                                    fontWeight = FontWeight.Light,
                                    lineHeight = 30.sp,
                                )
                            },
                            textStyle = typography.bodySmall.copy(lineHeight = 30.sp),
                            shape = RoundedCornerShape(10.dp),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Light2,
                                    focusedContainerColor = Light2,
                                    focusedBorderColor = LightBlue,
                                    unfocusedBorderColor = Color.Transparent,
                                ),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                onAddClick(text)
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),
                        ) {
                            Text(stringResource(R.string.add))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailsAttendeeSectionPreview() {
    MyApplicationTheme {
        DetailsAttendeeSection(
            attendees = Attendee.DUMMY_LIST.toImmutableList(),
            DetailsAttendeeSectionTabOption.ALL,
            isEdit = false,
            {},
            {},
            {},
            creatorId = Attendee.DUMMY_LIST.first().userId,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailsAttendeeSectionIsEditPreview() {
    MyApplicationTheme {
        DetailsAttendeeSection(
            attendees = Attendee.DUMMY_LIST.toImmutableList(),
            DetailsAttendeeSectionTabOption.ALL,
            isEdit = true,
            {},
            {},
            {},
            creatorId = Attendee.DUMMY_LIST.first().userId,
        )
    }
}
