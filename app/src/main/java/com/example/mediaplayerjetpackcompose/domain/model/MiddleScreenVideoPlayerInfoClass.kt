package com.example.mediaplayerjetpackcompose.domain.model

sealed class MiddleScreenVideoPlayerInfoClass() {
  data object Initial : MiddleScreenVideoPlayerInfoClass()
  data class Seek(var seekPos: Long) : MiddleScreenVideoPlayerInfoClass()
  data class FastSeek(var text: String, var icon: Int) : MiddleScreenVideoPlayerInfoClass()
}