package com.example.tasky.android.agenda.domain

import android.net.Uri

interface IImageCompressor {
    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long,
    ): ByteArray?
}
