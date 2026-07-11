package com.shermanrex.core.model

data class PlayingMusicInfo(
    val title: String,
    val musicID: String,
    val artworkUri: String,
    val musicUri: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val bitrate: Int,
    val size: Long,
) {
    companion object {
        val Initial = PlayingMusicInfo(
            title = "",
            musicID = "",
            artworkUri = "",
            musicUri = "",
            artist = "",
            album = "",
            duration = 0,
            bitrate = 0,
            size = 0,
        )
    }
}
