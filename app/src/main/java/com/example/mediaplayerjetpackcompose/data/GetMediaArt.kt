package com.example.mediaplayerjetpackcompose.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.data.util.onIoDispatcher
import com.example.mediaplayerjetpackcompose.data.util.onMainDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

class GetMediaArt(
  private var context: Context,
) {

  suspend fun getMusicArt(uri: Uri, width: Int = 200, height: Int = 200): Bitmap {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    return onMainDispatcher {
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

  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun getVideoThumbNail(uri: Uri, position: Long): ImageBitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(context, uri)
    return withContext(Dispatchers.Default.limitedParallelism(1)) {
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

}



