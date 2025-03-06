package com.example.mediaplayerjetpackcompose.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavRoute {
  @Serializable
  data object VideoScreen : MainNavRoute
  @Serializable
  data object MusicScreen : MainNavRoute
//  @Serializable
//  data class VideoPlayerScreen(var videoUri:String="") : MainNavRoute
}