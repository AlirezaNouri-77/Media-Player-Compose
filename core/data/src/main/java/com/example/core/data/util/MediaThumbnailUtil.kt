package com.example.core.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.core.graphics.scale
import androidx.palette.graphics.Palette
import com.example.core.domain.api.MediaThumbnailUtilImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class MediaThumbnailUtil(
  private var context: Context,
  private var defaultDispatcher: CoroutineDispatcher,
  private var ioDispatcher: CoroutineDispatcher,
) : MediaThumbnailUtilImpl {

  override suspend fun getMusicThumbnail(uri: Uri, width: Int, height: Int): Bitmap? {
    return withContext(defaultDispatcher) {
      runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          context.contentResolver.loadThumbnail(
            uri,
            Size(width, height),
            null
          )
        } else {
          val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
          val byteArray = mediaMetadataRetriever.embeddedPicture
          val bitmap = BitmapFactory.decodeByteArray(
            byteArray,
            0,
            byteArray!!.size
          )
          mediaMetadataRetriever.close()
          bitmap.scale(width, height)
        }
      }.getOrNull()
    }
  }


  override suspend fun getVideoThumbNail(uri: Uri, position: Long): Bitmap? {
    return withContext(defaultDispatcher) {
      runCatching {
        val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          mediaMetadataRetriever.use {
            it.getScaledFrameAtTime(
              position * 1000,
              OPTION_CLOSEST_SYNC,
              VIDEO_WIDTH,
              VIDEO_HEIGHT,
            )
          }
        } else {
          var imageBitmap = mediaMetadataRetriever.getScaledFrameAtTime(
            position * 1000,
            OPTION_CLOSEST_SYNC,
            VIDEO_WIDTH,
            VIDEO_HEIGHT,
          )
          mediaMetadataRetriever.close()
          imageBitmap
        }
      }.getOrNull()
    }
  }

  override suspend fun getVideoThumbNail(uri: Uri): Bitmap? {

    return withContext(defaultDispatcher) {
      runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          context.contentResolver.loadThumbnail(
            uri,
            Size(
              VIDEO_WIDTH,
              VIDEO_HEIGHT
            ),
            null
          )
        } else {
          val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
          val byteArray = mediaMetadataRetriever.embeddedPicture
          val bitmap = BitmapFactory.decodeByteArray(
            byteArray,
            0,
            byteArray!!.size
          )
          mediaMetadataRetriever.close()
          bitmap.scale(VIDEO_WIDTH, VIDEO_HEIGHT)
        }
      }.getOrNull()
    }
  }

  override suspend fun getMainColorOfBitmap(bitmap: Bitmap?): Int {
    return withContext(ioDispatcher) {
      if (bitmap == null ) return@withContext DefaultColorPalette

      val palette = Palette.from(bitmap).generate()

      val vibrant = async { palette.getVibrantColor(DefaultColorPalette) }
      val lightVibrant = async { palette.getLightVibrantColor(DefaultColorPalette) }
      val lightMuted = async { palette.getLightMutedColor(DefaultColorPalette) }
      val darkMuted = async { palette.getDarkMutedColor(DefaultColorPalette) }
      val darkVibrant = async { palette.getDarkVibrantColor(DefaultColorPalette) }

      return@withContext listOf(vibrant, lightVibrant, lightMuted, darkVibrant, darkMuted)
        .awaitAll()
        .filterNot { it == DefaultColorPalette }
        .firstOrNull() ?: DefaultColorPalette
    }
  }

  companion object {
    val DefaultColorPalette = Color.LTGRAY
    const val VIDEO_WIDTH = 150
    const val VIDEO_HEIGHT = 150
  }

}



