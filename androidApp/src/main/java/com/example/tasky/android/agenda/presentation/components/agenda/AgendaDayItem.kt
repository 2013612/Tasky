package com.example.tasky.android.agenda.presentation.components.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.theme.DarkGray
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.android.theme.Orange

@Composable
fun AgendaDayItem(
    upperString: String,
    lowerString: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(
                    if (isSelected) Orange else Color.Transparent,
                    shape = RoundedCornerShape(100.dp),
                ).padding(8.dp)
                .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(upperString, style = typography.labelSmall, lineHeight = 13.2.sp, color = if (isSelected) DarkGray else Gray)
        Spacer(Modifier.height(8.dp))
        Text(lowerString, style = typography.titleMedium, lineHeight = 20.4.sp, color = DarkGray)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AgendaDayItemPreview() {
    MyApplicationTheme {
        AgendaDayItem("S", "5", false, {})
    }
}

@Preview
@Composable
private fun AgendaDayItemSelectedPreview() {
    MyApplicationTheme {
        AgendaDayItem("M", "6", true, {})
    }
}
