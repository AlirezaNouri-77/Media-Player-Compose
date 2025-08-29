package com.example.video_media3.model

import android.graphics.Bitmap
import android.net.Uri

interface VideoThumbnailUtilImpl {
    suspend fun getVideoThumbNail(uri: Uri, position: Long): Bitmap?

    suspend fun getVideoThumbNail(uri: Uri): Bitmap?
}
