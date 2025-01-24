package com.example.mediaplayerjetpackcompose.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class MusicMediaStoreRepository(
  private var contentResolver: ContentResolver,
) : MediaStoreRepositoryImpl<MusicModel> {

  override fun getMedia(): Flow<MediaStoreResult<MusicModel>> {
    return channelFlow {

      val resultList = buildList {
        contentResolver.query(
          uriMedia,
          MediaInfoArray,
          selection,
          null,
          sortOrder,
        )?.use { cursor ->

          val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
          val mimeTypesColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
          val dataPathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
          val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
          val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
          val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
          val bitrateColumn = if (Build.VERSION.SDK_INT >= Constant.API_30_R_ANDROID_11) cursor.getColumnIndex(MediaStore.Audio.Media.BITRATE) else 0
          val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
          val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
          val albumId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
          val folderNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)

          while (cursor.moveToNext()) {
            val id = idColumn.let { cursor.getLong(it) }
            val dataPath = dataPathColumn.let { cursor.getString(it) }
            val mimeType = mimeTypesColumn.let { cursor.getString(it) }
            val name = nameColumn.let { cursor.getString(it) }
            val duration = durationColumn.let { cursor.getInt(it) }
            val bitrate = if (Build.VERSION.SDK_INT >= Constant.API_30_R_ANDROID_11) bitrateColumn.let { cursor.getInt(it) } else 0
            val size = sizeColumn.let { cursor.getInt(it) }
            val artist = artistColumn.let { cursor.getString(it) }
            val album = albumColumn.let { cursor.getString(it) }
            val folderName = folderNameColumn.let { cursor.getString(it) }
            val albumArtUri = ContentUris.withAppendedId(albumArtPath, albumId.let { cursor.getLong(it) })
            val contentUri: Uri = ContentUris.withAppendedId(
              MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
              id
            )
            add(
              MusicModel(
                musicId = id,
                path = dataPath,
                uri = contentUri,
                mimeTypes = mimeType,
                name = name,
                duration = duration,
                size = size,
                artworkUri = albumArtUri,
                bitrate = bitrate,
                artist = artist,
                album = album,
                folderName = folderName,
              )
            )
          }

        }
      }

      send(MediaStoreResult.Result(resultList))

    }.flowOn(Dispatchers.IO)
  }

  companion object {

    var MediaInfoArray = buildList<String> {
      addAll(
        arrayOf(
          MediaStore.Audio.Media._ID,
          MediaStore.Audio.Media.MIME_TYPE,
          MediaStore.Audio.Media.DATA,
          MediaStore.Audio.Media.DISPLAY_NAME,
          MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
          MediaStore.Audio.Media.DURATION,
          MediaStore.Audio.Media.SIZE,
          MediaStore.Audio.Media.ALBUM,
          MediaStore.Audio.Media.ARTIST,
          MediaStore.Audio.Media.ALBUM_ID,
        )
      )
      if (Build.VERSION.SDK_INT >= Constant.API_30_R_ANDROID_11) {
        add(MediaStore.Audio.Media.BITRATE)
      }
    }.toTypedArray()

    val albumArtPath: Uri = Uri.parse("content://media/external/audio/albumart")
    var uriMedia: Uri = if (Build.VERSION.SDK_INT >= Constant.API_29_Q_ANDROID_10) {
      MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }
    val sortOrder = "${MediaStore.Downloads.DISPLAY_NAME} ASC"
    var selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
    val selectionArgs = arrayOf(
      TimeUnit.MILLISECONDS.convert(15, TimeUnit.SECONDS).toString()
    )

  }

}


