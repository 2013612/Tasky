package com.example.tasky.android.agenda.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.tasky.android.R
import com.example.tasky.android.theme.Green
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.MyApplicationTheme

enum class AgendaEditTextType(
    @StringRes val stringId: Int,
) {
    TITLE(stringId = R.string.edit_title),
    DESCRIPTION(stringId = R.string.edit_description),
}

@Composable
fun AgendaEditText(
    initialValue: String,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    type: AgendaEditTextType,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable {
        mutableStateOf(initialValue)
    }

    Column(modifier = modifier.background(Color.White)) {
        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = null)
            }
            Box(Modifier.weight(1f)) {
                Text(
                    stringResource(type.stringId),
                    style = typography.headlineSmall,
                    lineHeight = 12.sp,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            TextButton(onClick = {
                onSaveClick(text)
            }) {
                Text(
                    stringResource(R.string.save),
                    style = typography.bodyMedium,
                    lineHeight = 12.sp,
                    color = Green,
                )
            }
        }
        HorizontalDivider(color = Light2, thickness = 2.dp)
        TextField(
            text,
            onValueChange = {
                text = it
            },
            modifier = Modifier.fillMaxSize(),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
            textStyle =
                when (type) {
                    AgendaEditTextType.TITLE -> typography.displaySmall.copy(fontWeight = FontWeight.Normal)
                    AgendaEditTextType.DESCRIPTION -> typography.bodySmall
                },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AgendaEditTextPreview() {
    MyApplicationTheme {
        AgendaEditText("Title", {}, {}, AgendaEditTextType.TITLE, Modifier.fillMaxSize())
    }
}
