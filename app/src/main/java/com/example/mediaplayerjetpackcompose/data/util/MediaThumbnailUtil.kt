package com.example.mediaplayerjetpackcompose.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.example.mediaplayerjetpackcompose.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaThumbnailUtil(
  private var context: Context,
) {

  suspend fun getMusicArt(uri: Uri, width: Int = 200, height: Int = 200): Bitmap {
    return onDefaultDispatcher {
      val mediaMetadataRetriever = MediaMetadataRetriever()
      runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          context.contentResolver.loadThumbnail(
            uri,
            Size(width, height),
            null
          )
        } else {
          val byteArray = mediaMetadataRetriever.use { it.embeddedPicture }
          val bitmap = BitmapFactory.decodeByteArray(
            byteArray,
            0,
            byteArray!!.size
          )
          Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
      }.getOrElse {
        Bitmap.createScaledBitmap(
          AppCompatResources.getDrawable(context, R.drawable.icon_music_note)!!.toBitmap(),
          width,
          height,
          true
        )
      }
    }
  }


  suspend fun getVideoThumbNail(uri: Uri, position: Long): ImageBitmap? {
    return onDefaultDispatcher {
      val mediaMetadataRetriever = MediaMetadataRetriever().also { it.setDataSource(context, uri) }
      runCatching {
        mediaMetadataRetriever.use {
          it.getScaledFrameAtTime(
            position * 1000,
            OPTION_CLOSEST_SYNC,
            Constant.VIDEO_WIDTH,
            Constant.VIDEO_HEIGHT,
          )?.asImageBitmap()
        }
      }.getOrNull()
    }
  }

  suspend fun getVideoThumbNail(uri: Uri): Bitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    return onIoDispatcher {
      runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          context.contentResolver.loadThumbnail(
            uri,
            Size(Constant.VIDEO_WIDTH, Constant.VIDEO_HEIGHT),
            null
          )
        } else {
          val byteArray = mediaMetadataRetriever.use { it.embeddedPicture }
          val bitmap = BitmapFactory.decodeByteArray(
            byteArray,
            0,
            byteArray!!.size
          )
          Bitmap.createScaledBitmap(bitmap, Constant.VIDEO_WIDTH, Constant.VIDEO_HEIGHT, true)
        }
      }.getOrNull()
    }
  }

  companion object {

    suspend fun getMainColorOfBitmap(bitmap: Bitmap): Int {

      return withContext(Dispatchers.Default) {
        val palette = Palette.from(bitmap).generate()

        val vibrant = palette.getVibrantColor(DefaultColorPalette)
        val lightVibrant = palette.getLightVibrantColor(DefaultColorPalette)
        val lightMuted = palette.getLightMutedColor(DefaultColorPalette)
        val darkMuted = palette.getDarkMutedColor(DefaultColorPalette)
        val darkVibrant = palette.getDarkVibrantColor(DefaultColorPalette)
        return@withContext listOf(vibrant, lightVibrant, lightMuted, darkVibrant, darkMuted)
          .filterNot { it == DefaultColorPalette }
          .firstOrNull() ?: DefaultColorPalette
      }

    }

    val DefaultColorPalette = Color.LightGray.toArgb()
  }
}



