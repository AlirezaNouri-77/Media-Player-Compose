package com.example.mediaplayerjetpackcompose.domain.model.navigation

sealed class NavigationRouteModel(var route: String) {
  data object VideoScreen : NavigationRouteModel("VideoScreen")
  data object MusicScreen : NavigationRouteModel("MusicScreen")
  data object VideoPlayerScreen : NavigationRouteModel("PlayerScreen?uri={videoUri}")
}