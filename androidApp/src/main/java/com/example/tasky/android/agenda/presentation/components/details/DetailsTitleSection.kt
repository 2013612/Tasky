package com.example.tasky.android.agenda.presentation.components.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
fun DetailsTitleSection(
    title: String,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.outline_circle_24),
            contentDescription = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            title,
            style = typography.displaySmall,
            lineHeight = 25.sp,
            color = Black,
            modifier = Modifier.weight(1f),
        )

        if (isEdit) {
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun DetailsTitleSectionPreview() {
    MyApplicationTheme {
        DetailsTitleSection("Project X", true)
    }
}
