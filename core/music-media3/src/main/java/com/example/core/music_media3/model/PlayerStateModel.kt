package com.example.core.music_media3.model

import com.example.core.model.ActiveMusicInfo

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