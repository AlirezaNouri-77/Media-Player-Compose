package com.example.mediaplayerjetpackcompose.data.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.mediaplayerjetpackcompose.R

class GetMediaArt(
  private var context: Context,
) {

  private val mediaMetadataRetriever = MediaMetadataRetriever()

  fun getMusicArt(uri: Uri, width: Int, height: Int): Bitmap {
    return runCatching {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver.loadThumbnail(
          uri,
          Size(width, height),
          null
        )
      } else {
        val byteArray = mediaMetadataRetriever.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(
          byteArray,
          0,
          byteArray!!.size
        )
        Bitmap.createScaledBitmap(bitmap, width, height, true)
      }
    }.getOrElse {
      Bitmap.createScaledBitmap(
        AppCompatResources.getDrawable(context, R.drawable.icon_music_350)!!.toBitmap(),
        width,
        height,
        true
      )
    }
  }

  suspend fun getVideoThumbNail(uri: Uri): Bitmap? {
    return onIoDispatcher {
      runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          context.contentResolver.loadThumbnail(
            uri,
            Size(Constant.VIDEO_WIDTH, Constant.VIDEO_HEIGHT),
            null
          )
        } else {
          val byteArray = mediaMetadataRetriever.embeddedPicture
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



