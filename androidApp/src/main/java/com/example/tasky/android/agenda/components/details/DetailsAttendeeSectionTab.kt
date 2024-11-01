package com.example.tasky.android.agenda.components.details

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.DarkGray
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.MyApplicationTheme

enum class DetailsAttendeeSectionTabOption(
    @StringRes val textId: Int,
) {
    ALL(textId = R.string.all),
    GOING(textId = R.string.going),
    NOT_GOING(textId = R.string.not_going),
}

@Composable
fun DetailsAttendeeSectionTab(
    curTab: DetailsAttendeeSectionTabOption,
    onTabSelect: (DetailsAttendeeSectionTabOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        DetailsAttendeeSectionTabOption.entries.map {
            DetailsAttendeeSectionTabButton(
                textId = it.textId,
                isSelected = it == curTab,
                onClick = { onTabSelect(it) },
                modifier = Modifier.weight(1f).height(30.dp),
            )
        }
    }
}

@Composable
private fun DetailsAttendeeSectionTabButton(
    @StringRes textId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(if (isSelected) Black else Light2, shape = RoundedCornerShape(100.dp))
                .clickable(onClick = onClick),
    ) {
        Text(
            stringResource(textId),
            style = typography.labelLarge,
            lineHeight = 15.sp,
            color = if (isSelected) Color.White else DarkGray,
            modifier =
                Modifier.align(
                    Alignment.Center,
                ),
        )
    }
}

@Preview
@Composable
private fun DetailsAttendeeSectionTabPreview() {
    MyApplicationTheme {
        DetailsAttendeeSectionTab(DetailsAttendeeSectionTabOption.ALL, {})
    }
}
