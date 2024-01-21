package com.example.mediaplayerjetpackcompose.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class MusicMediaStoreRepository : MediaStoreRepositoryImpl<MusicMediaModel> {
  
  override suspend fun getMedia(mContentResolver: ContentResolver , context: Context): Flow<List<MusicMediaModel>> {
	
	val resultList = mutableListOf<MusicMediaModel>()
	
	return flow {
	  mContentResolver.query(
		uriMedia,
		MediaInfoArray,
		selection,
		null,
		sortOrder
	  )?.use { cursor ->
		
		val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
		val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
		val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
		val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
		val bitrateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)
		val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
		
		while (cursor.moveToNext()) {
		  val id = idColumn.let { cursor.getLong(it) }
		  val name = nameColumn.let { cursor.getString(it) }
		  val duration = durationColumn.let { cursor.getInt(it) }
		  val bitrate = bitrateColumn.let { cursor.getInt(it) }
		  val size = sizeColumn.let { cursor.getInt(it) }
		  val artist = artistColumn.let { cursor.getString(it) }
		  val contentUri: Uri = ContentUris.withAppendedId(
			MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
			id
		  )
		  val artworkByteArray = getImageArt(context, uriMedia)
		  
		  resultList.add(
			MusicMediaModel(
			  musicId = id,
			  uri = contentUri,
			  name = name,
			  duration = duration,
			  size = size,
			  artworkByteArray = artworkByteArray,
			  bitrate = bitrate,
			  artist = artist,
			)
		  )
		  
		}
		
	  }
	  this.emit(resultList)
	}.flowOn(Dispatchers.IO)
  }
  
  companion object {
	
	val MediaInfoArray = arrayOf(
	  MediaStore.Audio.Media._ID,
	  MediaStore.Audio.Media.DISPLAY_NAME,
	  MediaStore.Audio.Media.DURATION,
	  MediaStore.Audio.Media.SIZE,
	  MediaStore.Audio.Media.BITRATE,
	  MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
	  MediaStore.Audio.Media.ARTIST,
	  
	  )
	var uriMedia = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
	  MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
	} else {
	  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
	}
	val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
	var selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
	val selectionArgs = arrayOf(
	  TimeUnit.MILLISECONDS.convert(15, TimeUnit.SECONDS).toString()
	)
	
  }
  private fun getImageArt(context: Context, uri: Uri): ByteArray? {
	return runCatching {
	  val mediaMetadataRetriever = MediaMetadataRetriever()
	  mediaMetadataRetriever.setDataSource(context, uri)
	  mediaMetadataRetriever.embeddedPicture
	}.getOrNull()
  }
  
}