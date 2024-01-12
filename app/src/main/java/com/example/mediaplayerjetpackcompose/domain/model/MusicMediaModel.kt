package com.example.mediaplayerjetpackcompose.domain.model

import android.graphics.Bitmap
import android.net.Uri
import java.util.UUID

data class MusicMediaModel(
  val uri: Uri = Uri.EMPTY,
  val name: String = "Nothing Play",
  val duration: Int = 0,
  val size: Int = 0,
  val image: Bitmap? = null,
  val bitrate: Int = 0,
  val bucketDisplayName: String = "",
  val artist: String = "",
  val id: String = UUID.randomUUID().toString(),
)