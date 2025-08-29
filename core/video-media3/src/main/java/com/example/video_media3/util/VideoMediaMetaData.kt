package com.example.video_media3.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.core.model.VideoModel
import com.example.video_media3.model.VideoMediaMetaDataImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class VideoMediaMetaData(
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
                    uri = uri.toString(),
                    name = documentFile.name.toString(),
                    mimeType = mimeType.toString(),
                    duration = duration.toLong(),
                    size = size,
                    height = 0,
                    width = 0,
                )
            } else {
                null
            }
        }
    }
}
