package com.example.mediaplayerjetpackcompose.presentation.screen.music.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

sealed interface MusicNavigationRoute {
  @Serializable
  data object Home : MusicNavigationRoute

  @Serializable
  data object Album : MusicNavigationRoute

  @Serializable
  data object Artist : MusicNavigationRoute

  @Serializable
  data object Search : MusicNavigationRoute

  @Serializable
  data class Category(var name: String, var parentRoute: ParentRoute) : MusicNavigationRoute
}

@Keep
enum class ParentRoute {
  FOLDER, ARTIST, ALBUM
}