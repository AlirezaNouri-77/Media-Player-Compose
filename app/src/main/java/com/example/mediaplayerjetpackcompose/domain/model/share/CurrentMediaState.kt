package com.example.mediaplayerjetpackcompose.domain.model.share

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.media3.common.MediaMetadata

@Immutable
data class CurrentMediaState(
  var uri: Uri,
  val metaData: MediaMetadata,
  val mediaId: String,
  val isPlaying: Boolean,
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