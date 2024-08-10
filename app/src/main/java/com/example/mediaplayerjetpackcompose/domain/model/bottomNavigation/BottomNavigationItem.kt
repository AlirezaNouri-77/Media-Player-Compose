package com.example.mediaplayerjetpackcompose.domain.model.bottomNavigation

import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel

sealed class BottomNavigationItem(
  var title: String,
  var icon: Int,
  var route: BottomBarNavigationModel,
) {
  data object Video : BottomNavigationItem(
    title = "Video",
    icon = R.drawable.icon_video,
    route = BottomBarNavigationModel.VideoScreen,
  )

  data object Music : BottomNavigationItem(
    title = "Music",
    icon = R.drawable.icon_music_note,
    route = BottomBarNavigationModel.MusicScreen,
  )
}