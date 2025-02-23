package com.example.mediaplayerjetpackcompose.core.model;

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class MusicModel(
  val musicId: Long,
  val uri: Uri,
  val path: String,
  val mimeTypes: String,
  val name: String,
  val duration: Int,
  val size: Int = 0,
  val artworkUri: Uri,
  val bitrate: Int,
  val artist: String,
  val album: String,
  val folderName: String,
) {
  companion object {
    var Empty = MusicModel(
      musicId = 0,
      uri = Uri.EMPTY,
      path = "",
      mimeTypes = "",
      name = "",
      duration = 0,
      size = 0,
      artworkUri = Uri.EMPTY,
      bitrate = 0,
      artist = "",
      album = "",
      folderName = "",
    )
  }
}