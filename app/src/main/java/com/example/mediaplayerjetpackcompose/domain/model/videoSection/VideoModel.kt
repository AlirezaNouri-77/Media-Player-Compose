package com.example.mediaplayerjetpackcompose.domain.model.videoSection

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

@Stable
data class VideoModel(
  val videoId: Long,
  val uri: Uri,
  val name: String,
  val mimeType: String,
  val duration: Int,
  val size: Int,
  val height: Int,
  val width: Int,
)

fun VideoModel.toMediaItem(): MediaItem {
  return MediaItem.Builder().setUri(this.uri).setMediaId(videoId.toString()).setMediaMetadata(
    MediaMetadata.Builder()
      .setTitle(name)
      .setExtras(
        bundleOf(
          "DURATION" to this.duration,
        )
      )
      .build()
  ).build()
}


