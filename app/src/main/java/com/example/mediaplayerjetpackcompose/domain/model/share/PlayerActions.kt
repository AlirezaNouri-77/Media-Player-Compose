package com.example.mediaplayerjetpackcompose.domain.model.share

sealed interface PlayerActions {
  data object PausePlayer : PlayerActions
  data object ResumePlayer : PlayerActions
  data object MoveNextPlayer : PlayerActions
  data class OnFavoriteToggle(var mediaId: String) : PlayerActions
  data class OnMoveToIndex(var value: Int) : PlayerActions
  data class MovePreviousPlayer(var seekToStart: Boolean) : PlayerActions
  data class SeekTo(var value: Long) : PlayerActions
  data class OnRepeatMode(var value: Int) : PlayerActions
}