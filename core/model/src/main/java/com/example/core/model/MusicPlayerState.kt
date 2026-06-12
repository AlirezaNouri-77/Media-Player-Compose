package com.example.core.model

data class MusicPlayerState(
    val currentMusicInfo: CurrentMusicInfo,
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val playerRepeatMode: PlayerRepeatMode,
    val isShuffleMode: Boolean,
) {
    companion object {
        val Initial = MusicPlayerState(
            currentMusicInfo = CurrentMusicInfo.Initial,
            isPlaying = false,
            isBuffering = false,
            playerRepeatMode = PlayerRepeatMode.MODE_OFF,
            isShuffleMode = false,
        )
    }
}
