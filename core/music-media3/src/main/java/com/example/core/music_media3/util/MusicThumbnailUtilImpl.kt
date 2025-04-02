package com.example.core.music_media3.util

import android.graphics.Bitmap
import android.net.Uri

interface MusicThumbnailUtilImpl {
  suspend fun getMusicThumbnail(uri: Uri, width: Int = 200, height: Int = 200): Bitmap?
  suspend fun getMainColorOfBitmap(bitmap: Bitmap?): Int
}