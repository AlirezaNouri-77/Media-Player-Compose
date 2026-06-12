package com.example.core.model

data class CurrentMusicInfo(
    val title: String,
    val musicID: String,
    val artworkUri: String,
    val musicUri: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val bitrate: Int,
    val size: Long,
    val isFavorite: Boolean,
) {
    companion object {
        val Initial = CurrentMusicInfo(
            title = "",
            musicID = "",
            artworkUri = "",
            musicUri = "",
            artist = "",
            album = "",
            duration = 0,
            bitrate = 0,
            size = 0,
            isFavorite = false,
        )
    }
}
