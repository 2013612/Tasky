package com.example.tasky.android.agenda.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.android.R
import com.example.tasky.android.theme.MyApplicationTheme

@Composable
fun DetailsTopBar(
    title: String,
    isEdit: Boolean,
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onCloseClick) {
            Icon(Icons.Outlined.Close, contentDescription = null, tint = Color.White)
        }

        Box(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = typography.bodyMedium,
                color = Color.White,
                lineHeight = 12.sp,
                modifier =
                    Modifier.align(
                        Alignment.Center,
                    ),
            )
        }

        if (isEdit) {
            TextButton(onClick = onSaveClick) {
                Text(
                    stringResource(R.string.save),
                    style = typography.bodyMedium,
                    color = Color.White,
                    lineHeight = 12.sp,
                )
            }
        } else {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Outlined.Edit, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DetailsTopBarPreview() {
    MyApplicationTheme {
        DetailsTopBar("01 MARCH 2022", false, {}, {}, {})
    }
}
