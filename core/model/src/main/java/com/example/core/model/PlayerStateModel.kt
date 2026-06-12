package com.example.core.model

data class PlayerStateModel(
    val currentMediaInfo: ActiveMusicInfo,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val repeatMode: Int = 0,
    val isShuffleMode: Boolean = false,
) {
    companion object {
        val Initial = PlayerStateModel(
            isPlaying = false,
            currentMediaInfo = ActiveMusicInfo.Initial,
            isBuffering = false,
            repeatMode = 0,
        )
    }
}
