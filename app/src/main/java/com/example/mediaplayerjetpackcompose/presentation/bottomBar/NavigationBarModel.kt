package com.example.mediaplayerjetpackcompose.presentation.bottomBar

import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.navigation.MusicTopLevelDestination

enum class NavigationBarModel(val title: String, val icon: Int, val route: MusicTopLevelDestination) {
    Home("Home", R.drawable.icon_home, MusicTopLevelDestination.Home),
    Album("Album", R.drawable.icon_album, MusicTopLevelDestination.Album),
    Artist("Artist", R.drawable.icon_music_artist, MusicTopLevelDestination.Artist),
    Search("Search", R.drawable.icon_search_24, MusicTopLevelDestination.Search),
}
