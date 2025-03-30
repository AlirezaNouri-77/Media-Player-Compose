package com.example.feature.video.model

import com.example.feature.video.R

sealed interface MiddleVideoPlayerIndicator {
  data object Initial : MiddleVideoPlayerIndicator
  data class FastForward(val text: String = "15", val icon: Int = R.drawable.icon_fast_forward_24) : MiddleVideoPlayerIndicator
  data class FastRewind(val text: String = "15", val icon: Int = R.drawable.icon_fast_rewind_24) : MiddleVideoPlayerIndicator
}