package com.example.mediaplayerjetpackcompose.data.mapper

import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel

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