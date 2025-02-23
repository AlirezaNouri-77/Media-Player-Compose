package com.example.mediaplayerjetpackcompose.core.data.mapper

import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import com.example.mediaplayerjetpackcompose.util.Constant
import com.example.mediaplayerjetpackcompose.core.model.VideoModel

fun MusicModel.toMediaItem() =
  MediaItem.Builder().setMediaId(this.musicId.toString()).setUri(this.uri)
    .setMediaMetadata(
      MediaMetadata.Builder()
        .setTitle(this.name)
        .setArtworkUri(artworkUri)
        .setArtist(this.artist)
        .setExtras(
          bundleOf(
            Constant.DURATION_KEY to this.duration,
            Constant.BITRATE_KEY to this.bitrate,
            Constant.SIZE_KEY to this.size,
          )
        ).setTitle(this.name).build()
    ).build()


fun VideoModel.toMediaItem(): MediaItem {
  return MediaItem.Builder().setUri(this.uri).setMediaId(videoId.toString()).setMediaMetadata(
    MediaMetadata.Builder()
      .setTitle(name)
      .setExtras(
        bundleOf(
          Constant.DURATION_KEY to this.duration,
        )
      )
      .build()
  ).build()
}
