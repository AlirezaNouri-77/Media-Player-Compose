package com.example.video_media3.model

import androidx.compose.runtime.Immutable
import com.example.core.model.ActiveVideoInfo

@Immutable
data class VideoPlayerState(
    val currentMediaInfo: ActiveVideoInfo,
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val repeatMode: Int,
) {
    companion object {
        var Empty = VideoPlayerState(
            isPlaying = false,
            currentMediaInfo = ActiveVideoInfo.Empty,
            isBuffering = false,
            repeatMode = 0,
        )
    }
}
