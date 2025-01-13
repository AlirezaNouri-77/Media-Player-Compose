package com.example.mediaplayerjetpackcompose.domain.model.navigation

import kotlinx.serialization.Serializable

sealed interface MusicNavigationModel {
  @Serializable
  data object Home : MusicNavigationModel

  @Serializable
  data object Album : MusicNavigationModel

  @Serializable
  data object Artist : MusicNavigationModel

  @Serializable
  data object Search : MusicNavigationModel

  @Serializable
  data class Category(var name: String, var parentRoute: ParentRoute) : MusicNavigationModel
}

enum class ParentRoute {
  FOLDER, ARTIST, ALBUM
}