package com.example.core.model.navigation

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
  data class Category(var name: String, var parentCategoryRoute: ParentCategoryRoute) : MusicNavigationRoute
}

enum class ParentCategoryRoute {
  FOLDER, ARTIST, ALBUM
}