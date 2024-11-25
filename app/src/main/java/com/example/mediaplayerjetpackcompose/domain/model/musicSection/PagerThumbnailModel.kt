package com.example.mediaplayerjetpackcompose.domain.model.musicSection

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class PagerThumbnailModel(
  val uri: Uri,
  val musicId: Long,
  val name:String,
  val artist:String,
)