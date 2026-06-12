package com.example.core.model

data class MusicPlayerState(
    val currentMusicInfo: CurrentMusicInfo,
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val repeatMode: Int,
    val isShuffleMode: Boolean,
) {
    companion object {
        val Initial = MusicPlayerState(
            currentMusicInfo = CurrentMusicInfo.Initial,
            isPlaying = false,
            isBuffering = false,
            repeatMode = 0,
            isShuffleMode = false,
        )
    }
}
