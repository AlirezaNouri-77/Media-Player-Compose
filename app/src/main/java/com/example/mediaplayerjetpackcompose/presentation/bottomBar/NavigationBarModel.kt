package com.example.mediaplayerjetpackcompose.presentation.bottomBar

import com.example.mediaplayerjetpackcompose.R
import com.example.core.model.navigation.MusicNavigationRoute

enum class NavigationBarModel(val title: String, val icon: Int, val route: MusicNavigationRoute) {
  Home("Home", R.drawable.icon_home, MusicNavigationRoute.Home),
  Album("Album", R.drawable.icon_album, MusicNavigationRoute.Album),
  Artist("Artist", R.drawable.icon_music_artist, MusicNavigationRoute.Artist),
  Search("Search", R.drawable.icon_search_24, MusicNavigationRoute.Search),
}