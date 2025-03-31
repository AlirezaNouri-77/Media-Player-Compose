package com.example.video_media3.model.mapper

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.core.model.ActiveVideoInfo
import com.example.core.model.VideoModel

fun MediaItem.toActiveVideoInfo() = ActiveVideoInfo(
  title = this.mediaMetadata.title.toString(),
  videoID = this.mediaId,
  videoUri = (this.localConfiguration?.uri ?: Uri.EMPTY).toString(),
  duration = this.mediaMetadata.extras?.getLong(MEDIA_METADATA_BUNDLE_DURATION_KEY) ?: 0L,
)

fun VideoModel.toMediaItem(): MediaItem {
  return MediaItem.Builder().setUri(this.uri).setMediaId(videoId.toString()).setMediaMetadata(
    MediaMetadata.Builder()
      .setTitle(name)
      .setExtras(
        bundleOf(
          MEDIA_METADATA_BUNDLE_DURATION_KEY to this.duration,
        )
      )
      .build()
  ).build()
}

const val MEDIA_METADATA_BUNDLE_DURATION_KEY = "Duration"