package com.example.core.data.model

data class ActiveVideoInfo(
  var title: String,
  var videoID: String,
  var videoUri: String,
  var duration: Long,
) {
  companion object {
    var Empty = ActiveVideoInfo(
      title = "", videoID = "", videoUri = "", duration = 0,
    )
  }
}