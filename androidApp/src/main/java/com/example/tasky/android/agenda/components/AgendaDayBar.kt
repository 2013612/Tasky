package com.example.tasky.android.agenda.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.android.theme.MyApplicationTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun AgendaDayBar(
    days: ImmutableList<Instant>,
    selectedDayOffset: Int,
    onDaySelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        itemsIndexed(days) { index, day ->
            val localDate = day.toLocalDateTime(TimeZone.currentSystemDefault())
            AgendaDayItem(
                upperString =
                    localDate.dayOfWeek.name
                        .first()
                        .toString(),
                lowerString = localDate.dayOfMonth.toString(),
                index == selectedDayOffset,
                onClick = { onDaySelect(index) },
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun AgendaDayBarPreview() {
    MyApplicationTheme {
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
