package com.example.mediaplayerjetpackcompose.domain.model

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

data class MusicMediaModel(
  val musicId: Long,
  val uri: Uri,
  val name: String,
  val duration: Int,
  val size: Int = 0,
  val artworkUri: Uri,
  val bitrate: Int,
  val artist: String,
  val album: String,
)

fun MusicMediaModel.toMediaItem() =
  MediaItem.Builder().setMediaId(this.musicId.toString()).setUri(this.uri)
    .setMediaMetadata(
      MediaMetadata.Builder()
        .setTitle(this.name)
        .setArtworkUri(artworkUri)
        .setArtist(this.artist)
        .setExtras(
          bundleOf(
            "Duration" to this.duration,
          )
        ).setTitle(this.name).build()
    ).build()
