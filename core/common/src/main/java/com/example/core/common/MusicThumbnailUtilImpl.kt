package com.example.core.common

import android.graphics.Bitmap
import android.net.Uri

interface MusicThumbnailUtilImpl {
    suspend fun getMusicThumbnail(uri: Uri, width: Int = 200, height: Int = 200): Bitmap?

    suspend fun getMainColorOfBitmap(bitmap: Bitmap?): Int
}
