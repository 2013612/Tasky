package com.example.tasky.android.agenda.presentation.details.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
fun DetailsDescSection(
    desc: String,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            desc,
            style = typography.bodySmall,
            lineHeight = 15.sp,
            color = Black,
            modifier = Modifier.weight(1f),
        )

        if (isEdit) {
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DetailsDescSectionPreview() {
    MyApplicationTheme {
        DetailsDescSection(
            "Weekly plan\n" +
                "Role distribution",
            true,
        )
    }
}
