package com.example.core.model

data class MusicModel(
    val musicId: Long,
    val uri: String,
    val path: String,
    val mimeTypes: String,
    val name: String,
    val duration: Long,
    val size: Long,
    val artworkUri: String,
    val bitrate: Int,
    val artist: String,
    val album: String,
    val folderName: String,
    val isFavorite: Boolean = false,
) {
    companion object {
        var Empty = MusicModel(
            musicId = 0,
            uri = "",
            path = "",
            mimeTypes = "",
            name = "",
            duration = 0,
            size = 0,
            artworkUri = "",
            bitrate = 0,
            artist = "",
            album = "",
            folderName = "",
        )
        var Dummy = MusicModel(
            musicId = 0,
            uri = "",
            path = "",
            mimeTypes = "",
            name = "example music name.mp3",
            duration = 240_000,
            size = 0,
            artworkUri = "",
            bitrate = 0,
            artist = "example artist",
            album = "",
            folderName = "",
        )
    }
}
