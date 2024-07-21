package com.example.mediaplayerjetpackcompose.domain.model.musicScreen

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
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
  var artist: String,
  val album: String,
)