package com.example.mediaplayerjetpackcompose.domain.model.videoSection

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class VideoItemModel(
  val videoId: Long,
  val uri: Uri,
  val name: String,
  val mimeType: String,
  val duration: Int,
  val size: Int,
  val height: Int,
  val width: Int,
) {
  companion object {
    var Empty = VideoItemModel(
      videoId = 0,
      uri = Uri.EMPTY,
      name = "",
      mimeType = "",
      duration = 0,
      size = 0,
      height = 0,
      width = 0
    )
    var Dummy = VideoItemModel(
      videoId = 0,
      uri = Uri.EMPTY,
      name = "Example Video Name.mp4",
      mimeType = "",
      duration = 341_000,
      size = 220_000,
      height = 1920,
      width = 1080,
    )
  }
}
