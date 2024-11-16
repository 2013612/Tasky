package com.example.tasky.agenda.data.mapper

import com.example.tasky.agenda.data.model.RemotePhoto
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.database.model.PhotoSerialized

fun RemotePhoto.toPhoto() =
    Photo(
        key = key,
        url = url,
    )

fun PhotoSerialized.toPhoto() =
    Photo(
        key = key,
        url = url,
    )
