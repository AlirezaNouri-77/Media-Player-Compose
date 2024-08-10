package com.example.mediaplayerjetpackcompose.domain.model.navigation

sealed class BottomBarNavigationModel(var route: String) {
  data object VideoScreen : BottomBarNavigationModel("VideoScreen")
  data object MusicScreen : BottomBarNavigationModel("MusicScreen")
  data object VideoPlayerScreen : BottomBarNavigationModel("PlayerScreen?uri={videoUri}")
}