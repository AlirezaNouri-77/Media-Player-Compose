package com.example.mediaplayerjetpackcompose.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class VideoMediaStoreRepository {

  suspend fun getMedia(mContentResolver: ContentResolver): Flow<List<VideoMediaModel>> {

    val mListResult = mutableListOf<VideoMediaModel>()

    return flow {
      mContentResolver.query(
        uriMedia,
        MediaInfoArray,
        selection,
        selectionArgs,
        sortOrder
      )?.use { cursor ->

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
        val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)

        while (cursor.moveToNext()) {
          val id = idColumn.let { cursor.getLong(it) }
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
            VideoMediaModel(
              uri = contentUri,
              name = name,
              duration = duration,
              size = size,
              height = height,
              width = width,
            )
          )

        }
      }

      emit(mListResult)

    }.flowOn(Dispatchers.IO)

  }

//  override suspend fun getMediaInformationByUri(
//    mContentResolver: ContentResolver,
//    uri: Uri
//  ): Flow<VideoMediaModel> {
//
//    return flow {
//      mContentResolver.query(uri, null, null, null, null)?.use { cursor ->
//
//        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
//
//        cursor.moveToFirst()
//
//        val name = nameColumn.let { cursor.getString(it) }
//
//        emit(
//          VideoMediaModel(
//            name = name,
//          )
//        )
//
//      }
//    }.flowOn(Dispatchers.IO)
//
//
//  }


  companion object {

    val MediaInfoArray = arrayOf(
      MediaStore.Video.Media._ID,
      MediaStore.Video.Media.DISPLAY_NAME,
      MediaStore.Video.Media.DURATION,
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
      TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES).toString()
    )

  }

}

