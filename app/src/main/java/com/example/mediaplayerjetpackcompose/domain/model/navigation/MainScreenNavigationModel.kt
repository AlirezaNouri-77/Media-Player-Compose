package com.example.mediaplayerjetpackcompose.domain.model.navigation

import kotlinx.serialization.Serializable

sealed interface MainScreenNavigationModel {
  @Serializable
  data object VideoScreen : MainScreenNavigationModel
  @Serializable
  data object MusicScreen : MainScreenNavigationModel
  @Serializable
  data class VideoPlayerScreen(var videoUri:String="") : MainScreenNavigationModel
}