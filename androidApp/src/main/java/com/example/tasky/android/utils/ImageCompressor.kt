package com.example.tasky.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

interface IImageCompressor {
    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long,
    ): ByteArray?
}

class ImageCompressor(
    private val context: Context,
) : IImageCompressor {
    override suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long,
    ): ByteArray? {
        return withContext(Dispatchers.IO) {
            val mimeType = context.contentResolver.getType(contentUri)
            val inputBytes =
                context
                    .contentResolver
                    .openInputStream(contentUri)
                    ?.use { inputStream ->
                        inputStream.readBytes()
                    } ?: return@withContext null

            ensureActive()

            withContext(Dispatchers.Default) compressionContext@{
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)

                ensureActive()

                val compressFormat =
                    when (mimeType) {
                        "image/png" -> Bitmap.CompressFormat.PNG
                        "image/jpeg" -> Bitmap.CompressFormat.JPEG
                        "image/webp" ->
                            if (Build.VERSION.SDK_INT >= 30) {
                                Bitmap.CompressFormat.WEBP_LOSSLESS
                            } else {
                                Bitmap.CompressFormat.WEBP
                            }
                        else -> Bitmap.CompressFormat.JPEG
                    }

                var outputBytes: ByteArray
                var quality = 90

                do {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(compressFormat, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                    }
                } while (isActive &&
                    outputBytes.size > compressionThreshold &&
                    quality > 5 &&
                    compressFormat != Bitmap.CompressFormat.PNG
                )

                if (outputBytes.size > compressionThreshold) {
                    return@compressionContext null
                }

                outputBytes
            }
        }
    }
}
