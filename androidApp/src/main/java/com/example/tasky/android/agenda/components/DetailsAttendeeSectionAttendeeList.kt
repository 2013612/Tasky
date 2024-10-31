package com.example.tasky.android.agenda.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.DarkGray
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.Attendee
import com.example.tasky.util.getAvatarDisplayName
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun DetailsAttendeeSectionAttendeeList(
    @StringRes titleId: Int,
    attendees: ImmutableList<Attendee>,
    onDeleteIconClick: (String) -> Unit,
    isEdit: Boolean,
    creatorId: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(stringResource(titleId), style = typography.bodyMedium, lineHeight = 15.sp, color = DarkGray)
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            attendees.map {
                Row(
                    modifier =
                        Modifier
                            .height(46.dp)
                            .background(Light2, shape = RoundedCornerShape(10.dp))
                            .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(32.dp)
                                .background(Gray, CircleShape),
                    ) {
                        Text(
                            it.fullName.getAvatarDisplayName(),
                            style = typography.bodyMedium,
                            fontSize = 13.sp,
                            lineHeight = 15.6.sp,
                            color = Color.White,
                            modifier =
                                Modifier.align(
                                    Alignment.Center,
                                ),
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(it.fullName, style = typography.labelLarge, lineHeight = 12.sp, color = DarkGray, modifier = Modifier.weight(1f))

                    if (it.userId == creatorId) {
                        Text(
                            stringResource(R.string.creator),
                            style = typography.labelLarge,
                            lineHeight = 12.sp,
                            color = LightBlue,
                        )
                    } else if (isEdit) {
                        IconButton(onClick = { onDeleteIconClick(it.userId) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = null, tint = DarkGray)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailsAttendeeSectionAttendeeListPreview() {
    MyApplicationTheme {
        DetailsAttendeeSectionAttendeeList(
            R.string.going,
            Attendee.DUMMY_LIST.toImmutableList(),
            {},
            true,
            Attendee.DUMMY_LIST.first().userId,
        )
    }
}
