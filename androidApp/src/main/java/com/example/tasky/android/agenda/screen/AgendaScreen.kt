package com.example.tasky.android.agenda.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.tasky.android.agenda.components.AgendaDayBar
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.AgendaItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

fun NavGraphBuilder.agendaScreen() {
    composable<Agenda> {
    }
}

@Serializable
object Agenda

data class AgendaScreenState(
    val agendas: ImmutableList<AgendaItem> = persistentListOf(),
    val name: String = "",
    val startDate: Instant = Clock.System.now(),
    val numberOfDateShown: Int = 6,
    val selectedDateOffset: Int = 0,
)

@Composable
private fun AgendaScreen(
    state: AgendaScreenState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(Black)) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    state.startDate.toLocalDateTime(TimeZone.currentSystemDefault()).month.name,
                    style = typography.bodyLarge,
                    lineHeight = 19.2.sp,
                    color = Color.White,
                )
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.White)
            }

            Box(modifier = Modifier.size(36.dp).background(Light, CircleShape)) {
                Text(
                    state.name,
                    style = typography.bodyMedium,
                    fontSize = 13.sp,
                    lineHeight = 15.6.sp,
                    color = LightBlue,
                    modifier =
                        Modifier.align(
                            Alignment.Center,
                        ),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    ).padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            AgendaDayBar(
                (0..state.numberOfDateShown)
                    .map {
                        val now = state.startDate
                        now.plus(it, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                    }.toImmutableList(),
                state.selectedDateOffset,
                {},
                Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    MyApplicationTheme {
        AgendaScreen(state = AgendaScreenState())
    }
}
