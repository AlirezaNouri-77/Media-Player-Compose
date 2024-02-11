package com.example.mediaplayerjetpackcompose.domain.model

import android.graphics.Bitmap
import android.net.Uri
import java.util.UUID

data class VideoMediaModel(
  val uri: Uri,
  val name: String,
  val duration: Int,
  val size: Int,
  val height: Int,
  val width: Int,
)
