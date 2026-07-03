package com.shermanrex.video_media3.model

import androidx.compose.runtime.Immutable
import com.shermanrex.core.model.CurrentVideoInfo

@Immutable
data class VideoPlayerState(
    val currentVideoInfo: CurrentVideoInfo,
    val isPlaying: Boolean,
    val isBuffering: Boolean,
    val repeatMode: Int,
) {
    companion object {
        val Empty = VideoPlayerState(
            isPlaying = false,
            currentVideoInfo = CurrentVideoInfo.Empty,
            isBuffering = false,
            repeatMode = 0,
        )
    }
}
