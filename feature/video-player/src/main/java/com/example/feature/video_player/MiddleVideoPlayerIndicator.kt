package com.example.feature.video_player

import com.example.feature.video.R

sealed interface MiddleVideoPlayerIndicator {
  data object Initial : MiddleVideoPlayerIndicator
  data class Seek(var seekPos: Long) : MiddleVideoPlayerIndicator
  data class FastSeek(var seekMode: FastSeekMode) : MiddleVideoPlayerIndicator
}

enum class FastSeekMode(var message: String, var icon: Int) {
  FastForward("15", R.drawable.icon_fast_forward_24),
  FastRewind("15", R.drawable.icon_fast_rewind_24),
}