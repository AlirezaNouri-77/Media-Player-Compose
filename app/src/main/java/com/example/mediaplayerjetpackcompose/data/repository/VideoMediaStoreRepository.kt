package com.example.mediaplayerjetpackcompose.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class VideoMediaStoreRepository(
  private var contentResolver: ContentResolver,
) : MediaStoreRepositoryImpl<VideoModel> {
  override suspend fun getMedia(): Flow<MediaStoreResult<out VideoModel>> {
    val mListResult = mutableListOf<VideoModel>()
    return flow {

      emit(MediaStoreResult.Loading)

      contentResolver.query(
        uriMedia,
        MediaInfoArray,
        selection,
        selectionArgs,
        sortOrder
      )?.use { cursor ->

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
        val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)

        while (cursor.moveToNext()) {
          val id = idColumn.let { cursor.getLong(it) }
          val mimeType = mimeColumn.let { cursor.getString(it) }
          val name = nameColumn.let { cursor.getString(it) }
          val duration = durationColumn.let { cursor.getInt(it) }
          val size = sizeColumn.let { cursor.getInt(it) }
          val height = heightColumn.let { cursor.getInt(it) }
          val width = widthColumn.let { cursor.getInt(it) }
          val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            id
          )

          mListResult.add(
            VideoModel(
              videoId = id,
              uri = contentUri,
              mimeType = mimeType,
              name = name,
              duration = duration,
              size = size,
              height = height,
              width = width,
            )
          )

        }
      }

      emit(MediaStoreResult.Result(mListResult))

    }.flowOn(Dispatchers.IO)

  }

  companion object {

    val MediaInfoArray = arrayOf(
      MediaStore.Video.Media._ID,
      MediaStore.Video.Media.DISPLAY_NAME,
      MediaStore.Video.Media.DURATION,
      MediaStore.Video.Media.MIME_TYPE,
      MediaStore.Video.Media.SIZE,
      MediaStore.Video.Media.HEIGHT,
      MediaStore.Video.Media.WIDTH,
    )
    var uriMedia = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
      MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
    val selection = "${MediaStore.Video.Media.DURATION} >= ?"
    val selectionArgs = arrayOf(
      TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS).toString()
    )

  }

}

