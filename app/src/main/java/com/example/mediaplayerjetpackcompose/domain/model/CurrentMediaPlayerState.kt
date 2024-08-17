package com.example.mediaplayerjetpackcompose.domain.model

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.media3.common.MediaMetadata

@Stable
data class MediaCurrentState(
  var isPlaying: Boolean,
  var uri: Uri,
  val metaData: MediaMetadata,
  val mediaId: String,
  val isBuffering: Boolean,
) {
  companion object {
    var Empty: MediaCurrentState = MediaCurrentState(
      isPlaying = false,
      mediaId = "",
      metaData = MediaMetadata.EMPTY,
      uri = Uri.EMPTY,
      isBuffering = false,
    )
  }
}