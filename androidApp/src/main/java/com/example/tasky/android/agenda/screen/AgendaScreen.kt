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
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable

fun NavGraphBuilder.agendaScreen() {
    composable<Agenda> {
    }
}

@Serializable
object Agenda

@Composable
private fun AgendaScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().background(Black)) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("MARCH", style = typography.bodyLarge, lineHeight = 19.2.sp, color = Color.White)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.White)
            }

            Box(modifier = Modifier.size(36.dp).background(Light, CircleShape)) {
                Text(
                    "AB",
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
                Modifier.fillMaxSize().background(
                    Color.White,
                    RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                ).padding(top = 16.dp, start = 16.dp, end = 16.dp),
        ) {
            AgendaDayBar(
                (0..5)
                    .map {
                        val now = Clock.System.now()
                        now.plus(it, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                    }.toImmutableList(),
                0,
                {},
                Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun AgendaScreenPreview() {
    MyApplicationTheme {
        AgendaScreen()
    }
}
