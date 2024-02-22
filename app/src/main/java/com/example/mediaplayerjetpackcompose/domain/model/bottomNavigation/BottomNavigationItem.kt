package com.example.mediaplayerjetpackcompose.domain.model.bottomNavigation

import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel

sealed class BottomNavigationItem(
  var title: String,
  var iconFilled: Int,
  var iconOutline: Int,
  var route: BottomBarNavigationModel,
) {
  data object Video : BottomNavigationItem(
    title = "Video",
    iconFilled = R.drawable.icon_video_filled,
    iconOutline = R.drawable.icon_video_outline,
    route = BottomBarNavigationModel.VideoScreen,
  )
  data object Music : BottomNavigationItem(
    title = "Music",
    iconFilled = R.drawable.icon_music_filled,
    iconOutline = R.drawable.icon_music_outline,
    route = BottomBarNavigationModel.MusicScreen,
  )
}