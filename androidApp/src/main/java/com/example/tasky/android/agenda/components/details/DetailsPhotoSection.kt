package com.example.tasky.android.agenda.components.details

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.android.R
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Gray
import com.example.tasky.android.theme.Light2
import com.example.tasky.android.theme.LightBlue
import com.example.tasky.android.theme.MyApplicationTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

sealed interface DetailsPhoto {
    data class RemotePhoto(
        val photo: Photo,
    ) : DetailsPhoto

    data class LocalPhoto(
        val key: String,
        val uri: Uri,
    ) : DetailsPhoto
}

@Composable
fun DetailsPhotoSection(
    photos: ImmutableList<DetailsPhoto>,
    showAddButton: Boolean,
    onAddPhoto: (Uri) -> Unit,
    onPhotoClick: (DetailsPhoto) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                onAddPhoto(it)
            }
        }

    Box(modifier = modifier.background(Light2)) {
        if (photos.isEmpty()) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .clickable(onClick = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null, tint = Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    stringResource(R.string.add_photos),
                    style = typography.bodyMedium,
                    lineHeight = 18.sp,
                    color = Gray,
                )
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(R.string.photos),
                    style = typography.headlineMedium,
                    lineHeight = 18.sp,
                    color = Black,
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(photos, key = {
                        when (it) {
                            is DetailsPhoto.RemotePhoto -> it.photo.key
                            is DetailsPhoto.LocalPhoto -> it.key
                        }
                    }) {
                        AsyncImage(
                            model =
                                when (it) {
                                    is DetailsPhoto.RemotePhoto -> it.photo.url
                                    is DetailsPhoto.LocalPhoto -> it.uri
                                },
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(60.dp)
                                    .border(
                                        width = 3.dp,
                                        color = LightBlue,
                                        shape = RoundedCornerShape(5.dp),
                                    ).clickable(onClick = { onPhotoClick(it) })
                                    .animateItem(),
                        )
                    }

                    if (showAddButton) {
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .size(
                                            60.dp,
                                        ).border(
                                            width = 3.dp,
                                            color = LightBlue,
                                            shape = RoundedCornerShape(5.dp),
                                        ).clickable(onClick = {
                                            pickMedia.launch(
                                                PickVisualMediaRequest(
                                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                                ),
                                            )
                                        }),
                            ) {
                                Icon(
                                    Icons.Outlined.Add,
                                    contentDescription = null,
                                    tint = LightBlue,
                                    modifier =
                                        Modifier.align(
                                            Alignment.Center,
                                        ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailsPhotoSectionPreview() {
    MyApplicationTheme {
        DetailsPhotoSection(
            photos = Photo.DUMMY_LIST.map { DetailsPhoto.RemotePhoto(it) }.toImmutableList(),
            true,
            {},
            {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun DetailsPhotoSectionEmptyPreview() {
    MyApplicationTheme {
        DetailsPhotoSection(
            photos = persistentListOf(),
            true,
            {},
            {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(110.dp),
        )
    }
}
