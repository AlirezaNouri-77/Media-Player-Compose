package com.example.mediaplayerjetpackcompose.domain.model.musicSection

import android.net.Uri

data class PagerThumbnailModel(
  val uri: Uri,
  val musicId: Long,
  val name:String,
  val artist:String,
)