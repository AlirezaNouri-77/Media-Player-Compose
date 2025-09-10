package com.example.core.music_media3.mapper

import android.net.Uri
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.core.model.ActiveMusicInfo
import com.example.core.model.MusicModel

fun MusicModel.toMediaItem() =
    MediaItem.Builder()
        .setMediaId(this.musicId.toString())
        .setUri(this.uri)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(this.name)
                .setArtworkUri(artworkUri.toUri())
                .setArtist(this.artist)
                .setAlbumTitle(this.album)
                .setExtras(
                    bundleOf(
                        MEDIAMETADATA_BUNDLE_DURATION_KEY to this.duration,
                        MEDIAMETADATA_BUNDLE_BITRATE_KEY to this.bitrate,
                        MEDIAMETADATA_BUNDLE_SIZE_KEY to this.size,
                        MEDIAMETADATA_BUNDLE_ISFAVORITE_KEY to this.isFavorite,
                        MEDIAMETADATA_BUNDLE_FOLDER_KEY to this.folderName,
                    ),
                ).build(),
        ).build()

fun MediaItem.toMusicModel() = MusicModel(
    musicId = this.mediaId.toLong(),
    uri = (this.localConfiguration?.uri ?: Uri.EMPTY).toString(),
    mimeTypes = (this.localConfiguration?.mimeType ?: Uri.EMPTY).toString(),
    name = (this.mediaMetadata.title ?: "None").toString(),
    duration = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_DURATION_KEY) ?: 0L,
    size = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_SIZE_KEY) ?: 0L,
    artworkUri = (this.mediaMetadata.artworkUri ?: Uri.EMPTY).toString(),
    bitrate = this.mediaMetadata.extras?.getInt(MEDIAMETADATA_BUNDLE_BITRATE_KEY) ?: 0,
    artist = (this.mediaMetadata.artist ?: "None").toString(),
    isFavorite = this.mediaMetadata.extras?.getBoolean(MEDIAMETADATA_BUNDLE_ISFAVORITE_KEY) ?: false,
    path = (this.mediaMetadata.title ?: "None").toString(),
    album = (this.mediaMetadata.albumTitle ?: "None").toString(),
    folderName = this.mediaMetadata.extras?.getString(MEDIAMETADATA_BUNDLE_FOLDER_KEY) ?: "None",
)

fun MediaItem.toActiveMusicInfo() = ActiveMusicInfo(
    title = (this.mediaMetadata.title ?: "None").toString(),
    musicID = this.mediaId,
    artworkUri = (this.mediaMetadata.artworkUri ?: Uri.EMPTY).toString(),
    musicUri = (this.localConfiguration?.uri ?: Uri.EMPTY).toString(),
    artist = this.mediaMetadata.artist.toString(),
    album = this.mediaMetadata.albumTitle.toString(),
    duration = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_DURATION_KEY) ?: 0L,
    bitrate = this.mediaMetadata.extras?.getInt(MEDIAMETADATA_BUNDLE_BITRATE_KEY) ?: 0,
    size = this.mediaMetadata.extras?.getLong(MEDIAMETADATA_BUNDLE_SIZE_KEY) ?: 0L,
    isFavorite = this.mediaMetadata.extras?.getBoolean(MEDIAMETADATA_BUNDLE_ISFAVORITE_KEY) ?: false,
)

const val MEDIAMETADATA_BUNDLE_DURATION_KEY = "Duration"
const val MEDIAMETADATA_BUNDLE_BITRATE_KEY = "Bitrate"
const val MEDIAMETADATA_BUNDLE_SIZE_KEY = "Size"
const val MEDIAMETADATA_BUNDLE_ISFAVORITE_KEY = "Favorite"
const val MEDIAMETADATA_BUNDLE_FOLDER_KEY = "Folder"
