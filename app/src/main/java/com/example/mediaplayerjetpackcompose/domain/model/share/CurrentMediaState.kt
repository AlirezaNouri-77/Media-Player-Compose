package com.example.mediaplayerjetpackcompose.domain.model.share

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.media3.common.MediaMetadata

@Stable
data class CurrentMediaState(
  var isPlaying: Boolean,
  var uri: Uri,
  val metaData: MediaMetadata,
  val mediaId: String,
  val isBuffering: Boolean,
  val repeatMode: Int,
) {
  companion object {
    var Empty: CurrentMediaState = CurrentMediaState(
      isPlaying = false,
      mediaId = "",
      metaData = MediaMetadata.EMPTY,
      uri = Uri.EMPTY,
      isBuffering = false,
      repeatMode = 0,
    )
  }
}