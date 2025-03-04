package com.example.feature.video.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.media3.common.MediaMetadata

@Immutable
data class VideoPlayerState(
  val uri: Uri,
  val metaData: MediaMetadata,
  val mediaId: String,
  val isPlaying: Boolean,
  val isBuffering: Boolean,
  val repeatMode: Int,
) {
  companion object {
    var Empty = VideoPlayerState(
      isPlaying = false,
      metaData = MediaMetadata.EMPTY,
      isBuffering = false,
      repeatMode = 0,
      uri = Uri.EMPTY,
      mediaId = "",
    )
  }
}