package com.example.feature.video.model

import com.example.feature.video.R

sealed interface VideoPlayerOverlayState {
    data object Initial : VideoPlayerOverlayState

    data class FastForward(val text: String = "15", val icon: Int = R.drawable.icon_fast_forward_24) : VideoPlayerOverlayState

    data class FastRewind(val text: String = "15", val icon: Int = R.drawable.icon_fast_rewind_24) : VideoPlayerOverlayState
}
