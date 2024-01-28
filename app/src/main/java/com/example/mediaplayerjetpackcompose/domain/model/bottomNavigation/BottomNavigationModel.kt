package com.example.mediaplayerjetpackcompose.domain.model.bottomNavigation

import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.screen.NavigationRoute

sealed class BottomNavigationModel(
  var title: String,
  var icon: Int,
  var route: NavigationRoute,
) {

  data object Video : BottomNavigationModel(
    title = "Video",
    icon = R.drawable.icon_navigation_video,
    route = NavigationRoute.VideoScreen,
  )

  data object Music : BottomNavigationModel(
    title = "Music",
    icon = R.drawable.icon_navigation_music,
    route = NavigationRoute.MusicScreen,
  )
}