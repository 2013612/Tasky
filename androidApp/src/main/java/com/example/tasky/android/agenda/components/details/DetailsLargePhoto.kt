package com.example.tasky.android.agenda.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.MyApplicationTheme
import com.example.tasky.model.agenda.Photo

@Composable
fun DetailsLargePhoto(
    photo: Photo,
    isEdit: Boolean,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(Black)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Outlined.Close, contentDescription = null, tint = Color.White)
            }
            Text(stringResource(R.string.photo), style = typography.bodyMedium, lineHeight = 12.sp, color = Color.White)
            IconButton(onClick = onDeleteClick, modifier = Modifier.alpha(if (isEdit) 1f else 0f), enabled = isEdit) {
                Icon(Icons.Outlined.Delete, contentDescription = null, tint = Color.White)
            }
        }
        AsyncImage(model = photo.url, contentDescription = null, modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun DetailsLargePhotoPreview() {
    MyApplicationTheme {
        DetailsLargePhoto(Photo.DUMMY_LIST.first(), false, {}, {}, Modifier.fillMaxSize())
    }
}

@Preview
@Composable
private fun DetailsLargePhotoEditPreview() {
    MyApplicationTheme {
        DetailsLargePhoto(Photo.DUMMY_LIST.first(), true, {}, {}, Modifier.fillMaxSize())
    }
}
