package com.example.mediaplayerjetpackcompose.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.mediaplayerjetpackcompose.domain.api.MetaDataRetrieverImpl
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMediaMetaData(
  private var context: Context,
) : MetaDataRetrieverImpl<VideoItemModel?> {

  override suspend fun get(uri: Uri): VideoItemModel? {
    return withContext(Dispatchers.IO) {
      val documentFile = DocumentFile.fromSingleUri(context, uri) ?: return@withContext null

      val mediaMetadataRetriever = MediaMetadataRetriever().also { it.setDataSource(context, documentFile.uri) }

      val mimeType = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
      val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
      val size = documentFile.length().toInt()

      return@withContext if (duration != null && size > 0) {
        VideoItemModel(
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