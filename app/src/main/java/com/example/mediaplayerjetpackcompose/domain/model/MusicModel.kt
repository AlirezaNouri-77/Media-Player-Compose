package com.example.mediaplayerjetpackcompose.domain.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

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
  var artBitmap: Bitmap,
)

fun MusicModel.toMediaItem() =
  MediaItem.Builder().setMediaId(this.musicId.toString()).setUri(this.uri)
    .setMediaMetadata(
      MediaMetadata.Builder()
        .setTitle(this.name)
        .setArtworkUri(artworkUri)
        .setArtist(this.artist)
        .setExtras(
          bundleOf(
            "Duration" to this.duration,
            "Bitrate" to this.bitrate,
            "Size" to this.size,
          )
        ).setTitle(this.name).build()
    ).build()
