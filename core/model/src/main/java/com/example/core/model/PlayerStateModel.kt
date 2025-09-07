package com.example.core.model

data class PlayerStateModel(
    val currentMediaInfo: ActiveMusicInfo,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val repeatMode: Int = 0,
) {
    companion object {
        var Initial = PlayerStateModel(
            isPlaying = false,
            currentMediaInfo = ActiveMusicInfo.Empty,
            isBuffering = false,
            repeatMode = 0,
        )
    }
}
