package com.example.core.data.model

data class ActiveMusicInfo(
  var title: String,
  var musicID: String,
  var artworkUri: String,
  var musicUri: String,
  var artist: String,
  var duration: Long,
  var bitrate: Int,
  var size: Long,
) {
  companion object {
    var Empty = ActiveMusicInfo(
      title = "",
      musicID = "",
      artworkUri = "",
      musicUri = "",
      artist = "",
      duration = 0,
      bitrate = 0,
      size = 0,
    )
  }
}
