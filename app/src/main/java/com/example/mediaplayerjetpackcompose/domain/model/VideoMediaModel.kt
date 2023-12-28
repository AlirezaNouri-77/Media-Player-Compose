package com.example.mediaplayerjetpackcompose.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class VideoMediaModel(
  val uri: Uri = Uri.EMPTY,
  val name: String = "",
  val duration: Int = 0,
  val size: Int = 0,
  val image: Bitmap? = null,
)
