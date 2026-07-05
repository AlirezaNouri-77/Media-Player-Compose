package com.shermanrex.shermbeat.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.shermanrex.core.designsystem.R as designSystemRes
import com.shermanrex.shermbeat.R as mainRes

enum class MusicTopLevel(val title: String, val icon: Int, val route: NavKey) {
    Home("Home", mainRes.drawable.icon_home, HomeMusic),
    Album("Album", mainRes.drawable.icon_album, AlbumMusic),
    Artist("Artist", mainRes.drawable.icon_music_artist, ArtistMusic),
    Search("Search", designSystemRes.drawable.icon_search, SearchMusic),
}
