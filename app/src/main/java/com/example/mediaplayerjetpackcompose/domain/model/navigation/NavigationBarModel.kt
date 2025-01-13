package com.example.mediaplayerjetpackcompose.domain.model.navigation;

import com.example.mediaplayerjetpackcompose.R

enum class NavigationBarModel(var title: String, var icon: Int, var route: MusicNavigationModel) {
  Home("Home", R.drawable.icon_home, MusicNavigationModel.Home),
  Album("Album", R.drawable.icon_album, MusicNavigationModel.Album),
  Artist("Artist", R.drawable.icon_music_artist, MusicNavigationModel.Artist),
  Search("Search", R.drawable.icon_search_24, MusicNavigationModel.Search),
}