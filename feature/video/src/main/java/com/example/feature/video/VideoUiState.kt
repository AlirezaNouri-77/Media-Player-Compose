package com.example.feature.video

import com.example.core.model.VideoModel
import com.example.feature.video.model.VideoPlayerOverlayState
import com.example.video_media3.model.VideoPlayerState

data class VideoUiState(
    val loading: Boolean = false,
    val videoList: List<VideoModel> = emptyList(),
    val videoPlayerOverlayState: VideoPlayerOverlayState = VideoPlayerOverlayState.Initial,
    val isVideoPlayerOverlayControllerVisible: Boolean = false,
    val currentPlayerPosition: Long = 0,
    val videoPlayerState: VideoPlayerState = VideoPlayerState.Empty,
)