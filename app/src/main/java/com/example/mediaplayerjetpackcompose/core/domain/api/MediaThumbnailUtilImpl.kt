package com.example.mediaplayerjetpackcompose.core.domain.api

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

interface MediaThumbnailUtilImpl {
  suspend fun getMusicThumbnail(uri: Uri, width: Int = 200, height: Int = 200): Bitmap
  suspend fun getVideoThumbNail(uri: Uri, position: Long): ImageBitmap?
  suspend fun getVideoThumbNail(uri: Uri): Bitmap?
}