package com.shermanrex.core.model

data class PlayingMusicState(
    val playingMusicInfo: PlayingMusicInfo,
    val playerRepeatMode: PlayerRepeatMode,
    val isPlaying: Boolean,
    val isFavorite: Boolean,
    val isBuffering: Boolean,
    val isShuffleMode: Boolean,
) {
    companion object {
        val Initial = PlayingMusicState(
            playingMusicInfo = PlayingMusicInfo.Initial,
            isPlaying = false,
            isFavorite = false,
            isBuffering = false,
            playerRepeatMode = PlayerRepeatMode.MODE_OFF,
            isShuffleMode = false,
        )
    }
}
