package com.example.core.model

data class VideoModel(
  val videoId: Long,
  val uri: String,
  val name: String,
  val mimeType: String,
  val duration: Long,
  val size: Int,
  val height: Int,
  val width: Int,
) {
  companion object {
    var Empty = VideoModel(
      videoId = 0,
      uri = "",
      name = "",
      mimeType = "",
      duration = 0,
      size = 0,
      height = 0,
      width = 0
    )
    var Dummy = VideoModel(
      videoId = 0,
      uri = "",
      name = "Example Video Name.mp4",
      mimeType = "",
      duration = 341_000,
      size = 220_000,
      height = 1920,
      width = 1080,
    )
  }
}
