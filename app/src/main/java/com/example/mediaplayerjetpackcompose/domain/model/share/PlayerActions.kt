package com.example.mediaplayerjetpackcompose.domain.model.share

sealed interface PlayerActions {
  data object PausePlayer : PlayerActions
  data object ResumePlayer : PlayerActions
  data object MoveNextPlayer : PlayerActions
  data class OnFavoriteToggle(val mediaId: String) : PlayerActions
  data class OnMoveToIndex(val value: Int) : PlayerActions
  data class MovePreviousPlayer(val seekToStart: Boolean) : PlayerActions
  data class SeekTo(val value: Long) : PlayerActions
  data class OnRepeatMode(val value: Int) : PlayerActions
  data class UpdateArtworkPageIndex(val value: Int) : PlayerActions
}