package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.AlbumMusic
import com.example.mediaplayerjetpackcompose.presentation.ArtistMusic
import com.example.mediaplayerjetpackcompose.presentation.HomeMusic
import com.example.mediaplayerjetpackcompose.presentation.SearchMusic

enum class MusicTopLevel(val title: String, val icon: Int, val route: NavKey) {
    Home("Home", R.drawable.icon_home, HomeMusic),
    Album("Album", R.drawable.icon_album, AlbumMusic),
    Artist("Artist", R.drawable.icon_music_artist, ArtistMusic),
    Search("Search", R.drawable.icon_search_24, SearchMusic),
}
