package com.example.mediaplayerjetpackcompose.core.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.mediaplayerjetpackcompose.core.domain.api.VideoMediaMetaDataImpl
import com.example.mediaplayerjetpackcompose.core.model.VideoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class VideoMediaVideoMediaMetaData(
  private var context: Context,
  private var ioDispatcher: CoroutineDispatcher,
) : VideoMediaMetaDataImpl {

  override suspend fun get(uri: Uri): VideoModel? {
    return withContext(ioDispatcher) {
      val documentFile = DocumentFile.fromSingleUri(context, uri) ?: return@withContext null

      val mediaMetadataRetriever = MediaMetadataRetriever().also { it.setDataSource(context, documentFile.uri) }

      val mimeType = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
      val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
      val size = documentFile.length().toInt()

      return@withContext if (duration != null && size > 0) {
        VideoModel(
          videoId = 0,
          uri = uri,
          name = documentFile.name.toString(),
          mimeType = mimeType.toString(),
          duration = duration.toInt(),
          size = size,
          height = 0,
          width = 0,
        )
      } else null
    }
  }

}