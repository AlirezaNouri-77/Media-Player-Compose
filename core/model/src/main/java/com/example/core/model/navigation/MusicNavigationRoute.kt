package com.example.core.model.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
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

@Keep
enum class ParentCategoryRoute {
  FOLDER, ARTIST, ALBUM
}