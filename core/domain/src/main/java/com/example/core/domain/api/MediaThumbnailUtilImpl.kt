package com.example.core.domain.api

import android.graphics.Bitmap
import android.net.Uri

interface MediaThumbnailUtilImpl {
  suspend fun getMusicThumbnail(uri: Uri, width: Int = 200, height: Int = 200): Bitmap?
  suspend fun getVideoThumbNail(uri: Uri, position: Long): Bitmap?
  suspend fun getVideoThumbNail(uri: Uri): Bitmap?
  suspend fun getMainColorOfBitmap(bitmap: Bitmap?): Int
}