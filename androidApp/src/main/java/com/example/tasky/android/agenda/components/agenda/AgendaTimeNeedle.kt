package com.example.tasky.android.agenda.components.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
fun AgendaTimeNeedle(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Box(modifier = Modifier.width(11.34.dp).height(10.dp).background(Black, CircleShape))
        HorizontalDivider(color = Black, modifier = Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
private fun AgendaTimeNeedlePreview() {
    MyApplicationTheme {
        AgendaTimeNeedle()
    }
}
