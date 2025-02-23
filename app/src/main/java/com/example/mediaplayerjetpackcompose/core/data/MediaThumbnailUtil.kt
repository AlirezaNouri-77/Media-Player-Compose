package com.example.mediaplayerjetpackcompose.core.data

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
import com.example.mediaplayerjetpackcompose.core.domain.api.MediaThumbnailUtilImpl
import com.example.mediaplayerjetpackcompose.util.Constant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class MediaThumbnailUtil(
  private var context: Context,
  private var defaultDispatcher: CoroutineDispatcher,
  private var ioDispatcher: CoroutineDispatcher,
) : MediaThumbnailUtilImpl {

  override suspend fun getMusicThumbnail(uri: Uri, width: Int, height: Int): Bitmap {
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


  override suspend fun getVideoThumbNail(uri: Uri, position: Long): ImageBitmap? {
    return withContext(defaultDispatcher) {
      runCatching {
        val mediaMetadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          mediaMetadataRetriever.use {
            it.getScaledFrameAtTime(
              position * 1000,
              OPTION_CLOSEST_SYNC,
              Constant.VIDEO_WIDTH,
              Constant.VIDEO_HEIGHT,
            )?.asImageBitmap()
          }
        } else {
          var imageBitmap = mediaMetadataRetriever.getScaledFrameAtTime(
            position * 1000,
            OPTION_CLOSEST_SYNC,
            Constant.VIDEO_WIDTH,
            Constant.VIDEO_HEIGHT,
          )?.asImageBitmap()
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
            Size(Constant.VIDEO_WIDTH, Constant.VIDEO_HEIGHT),
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
          Bitmap.createScaledBitmap(bitmap, Constant.VIDEO_WIDTH, Constant.VIDEO_HEIGHT, true)
        }
      }.getOrNull()
    }
  }

  suspend fun getMainColorOfBitmap(bitmap: Bitmap): Int {
    return withContext(ioDispatcher) {
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
    val DefaultColorPalette = Color.LightGray.toArgb()
  }

}



