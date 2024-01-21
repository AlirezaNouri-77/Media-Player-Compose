package com.example.mediaplayerjetpackcompose.domain.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import java.util.UUID

data class MusicMediaModel(
  val musicId: Long,
  val uri: Uri,
  val name: String,
  val duration: Int,
  val size: Int = 0,
  val artworkByteArray: ByteArray?,
  val bitrate: Int,
  val artist: String,
)

fun MusicMediaModel.toMediaItem() =
  MediaItem.Builder().setMediaId(this.musicId.toString()).setUri(this.uri)
	.setMediaMetadata(
	  MediaMetadata.Builder()
		.setTitle(this.name)
		.setArtworkData(this.artworkByteArray, MediaMetadata.PICTURE_TYPE_MEDIA)
		.setArtist(this.artist).setExtras(
		  bundleOf(
			"Duration" to this.duration,
		  )
		).setTitle(this.name).build()
	).build()
