package com.example.mediaplayerjetpackcompose.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class MusicMediaStoreRepository : MediaStoreRepositoryImpl<MusicMediaModel> {

  override suspend fun getMedia(
    mContentResolver: ContentResolver,
    context: Context
  ): Flow<List<MusicMediaModel>> {

    val resultList = mutableListOf<MusicMediaModel>()

    return flow {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        mContentResolver.query(
          uriMedia,
          MediaInfoArray,
          selection,
          null,
          null
        )?.use { cursor ->

          val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
          val dataPathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
          val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
          val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
          val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
          val bitrateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)
          val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
          val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

//          val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
//          val dataPathColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA)
//          val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
//          val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DURATION)
//          val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.SIZE)
//          val bitrateColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.BITRATE)
//          val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.ARTIST)
//          val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.ALBUM)


          while (cursor.moveToNext()) {
            val id = idColumn.let { cursor.getLong(it) }
            val dataPath = dataPathColumn.let { cursor.getString(it) }
            val name = nameColumn.let { cursor.getString(it) }
            val duration = durationColumn.let { cursor.getInt(it) }
            val bitrate = bitrateColumn.let { cursor.getInt(it) }
            val size = sizeColumn.let { cursor.getInt(it) }
            val artist = artistColumn.let { cursor.getString(it) }
            val album = albumColumn.let { cursor.getString(it) }
            val contentUri: Uri = ContentUris.withAppendedId(
              MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
              id
            )
            resultList.add(
              MusicMediaModel(
                musicId = id,
                path = dataPath,
                uri = contentUri,
                name = name,
                duration = duration,
                size = size,
                artworkUri = contentUri,
                bitrate = bitrate,
                artist = artist,
                album = album,
              )
            )

          }

        }
      }
      this.emit(resultList)
    }.flowOn(Dispatchers.IO)
  }

  companion object {

    val MediaInfoArray = arrayOf(
      MediaStore.Audio.Media._ID,
      MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE,
      MediaStore.Audio.Media.BITRATE,
      MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.ARTIST,
      )
//    val MediaInfoArray = arrayOf(
//      MediaStore.Downloads._ID,
//      MediaStore.Downloads.DATA,
//      MediaStore.Downloads.DISPLAY_NAME,
//      MediaStore.Downloads.DURATION,
//      MediaStore.Downloads.SIZE,
//      MediaStore.Downloads.BITRATE,
//      MediaStore.Downloads.ALBUM,
//      MediaStore.Downloads.ARTIST,
//      )
    var uriMedia = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }
//@RequiresApi(Build.VERSION_CODES.Q)
//var uriMedia = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//      MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
//    } else {
//      MediaStore.Downloads.EXTERNAL_CONTENT_URI
//    }
    val sortOrder = "${MediaStore.Downloads.DISPLAY_NAME} ASC"
    var selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
    val selectionArgs = arrayOf(
      TimeUnit.MILLISECONDS.convert(15, TimeUnit.SECONDS).toString()
    )

  }

}