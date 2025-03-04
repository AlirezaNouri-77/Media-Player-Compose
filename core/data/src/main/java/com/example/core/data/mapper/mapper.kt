package com.example.core.data.mapper

import android.net.Uri
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.core.data.model.ActiveMusicInfo
import com.example.core.data.model.ActiveVideoInfo
import com.example.core.model.MusicModel
import com.example.core.model.VideoModel

fun MusicModel.toMediaItem() =
  MediaItem.Builder().setMediaId(this.musicId.toString()).setUri(this.uri)
    .setMediaMetadata(
      MediaMetadata.Builder()
        .setTitle(this.name)
        .setArtworkUri(artworkUri.toUri())
        .setArtist(this.artist)
        .setExtras(
          bundleOf(
            MEDIAMETADATA_BUNDLE_DURATION_KEY to this.duration,
            MEDIAMETADATA_BUNDLE_BITRATE_KEY to this.bitrate,
            MEDIAMETADATA_BUNDLE_SIZE_KEY to this.size,
          )
        ).build()
    ).build()

fun MediaItem.toActiveMusicInfo() = ActiveMusicInfo(
  title = (this.mediaMetadata.title ?: "None").toString(),
  musicID = this.mediaId,
  artworkUri = (this.mediaMetadata.artworkUri ?: Uri.EMPTY).toString(),
  musicUri = (this.localConfiguration?.uri ?: Uri.EMPTY).toString(),
  artist = this.mediaMetadata.artist.toString(),
  duration = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_DURATION_KEY) ?: 0L,
  bitrate = this.mediaMetadata.extras?.getInt(MEDIAMETADATA_BUNDLE_BITRATE_KEY) ?: 0,
  size = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_SIZE_KEY) ?: 0L,
)

fun MediaItem.toActiveVideoInfo() = ActiveVideoInfo(
  title = this.mediaMetadata.title.toString(),
  videoID = this.mediaId,
  videoUri = (this.localConfiguration?.uri ?: Uri.EMPTY).toString(),
  duration = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_DURATION_KEY) ?: 0L,
)

fun VideoModel.toMediaItem(): MediaItem {
  return MediaItem.Builder().setUri(this.uri).setMediaId(videoId.toString()).setMediaMetadata(
    MediaMetadata.Builder()
      .setTitle(name)
      .setExtras(
        bundleOf(
          MEDIAMETADATA_BUNDLE_DURATION_KEY to this.duration,
        )
      )
      .build()
  ).build()
}

const val MEDIAMETADATA_BUNDLE_DURATION_KEY = "Duration"
const val MEDIAMETADATA_BUNDLE_BITRATE_KEY = "Bitrate"
const val MEDIAMETADATA_BUNDLE_SIZE_KEY = "Size"