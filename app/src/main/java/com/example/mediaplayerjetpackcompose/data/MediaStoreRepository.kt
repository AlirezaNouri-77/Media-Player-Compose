package com.example.mediastore.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class MediaStoreRepository : MediaStoreRepositoryImpl {
  
  override suspend fun getMedia(mContentResolver: ContentResolver): Flow<List<VideoMediaModel>> {
	var mListResult = mutableListOf<VideoMediaModel>()
	return flow {
	  mContentResolver.query(
		uriMedia,
		MediaInfoArray,
		selection,
		selectionArgs,
		sortOrder
	  ).use { cursor ->
		
		val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
		val nameColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
		val durationColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
		val sizeColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
		
		if (cursor != null) {
		  while (cursor.moveToNext()) {
			val id = idColumn?.let { cursor.getLong(it) }
			val name = nameColumn?.let { cursor.getString(it) }
			val duration = durationColumn?.let { cursor.getInt(it) }
			val size = sizeColumn?.let { cursor.getInt(it) }
			val contentUri: Uri = ContentUris.withAppendedId(
			  MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
			  id!!
			)
			val imageBitmap = mContentResolver.loadThumbnail(contentUri, Size(640, 480), null)
			
			mListResult.add(
			  VideoMediaModel(
				uri = contentUri,
				name = name!!,
				duration = duration!!,
				size = size!!,
				image = imageBitmap
			  )
			)
			
		  }
		}
	  }
	  
	  emit(mListResult)
	  
	}.flowOn(Dispatchers.IO)
	
  }
  
  companion object {
	val MediaInfoArray = arrayOf(
	  MediaStore.Video.Media._ID,
	  MediaStore.Video.Media.DISPLAY_NAME,
	  MediaStore.Video.Media.DURATION,
	  MediaStore.Video.Media.SIZE,
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

