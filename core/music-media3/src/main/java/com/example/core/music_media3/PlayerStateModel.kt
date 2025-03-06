package com.example.core.music_media3

import androidx.compose.runtime.Immutable
import com.example.core.model.ActiveMusicInfo

@Immutable
data class PlayerStateModel(
  val currentMediaInfo: ActiveMusicInfo,
  val isPlaying: Boolean = false,
  val isBuffering: Boolean = false,
  val repeatMode: Int = 0,
) {
  companion object {
    var Empty = PlayerStateModel(
      isPlaying = false,
      currentMediaInfo = ActiveMusicInfo.Empty,
      isBuffering = false,
      repeatMode = 0,
    )
  }
}