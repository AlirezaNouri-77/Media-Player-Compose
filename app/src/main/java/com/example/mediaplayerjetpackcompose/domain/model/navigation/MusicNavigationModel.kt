package com.example.mediaplayerjetpackcompose.domain.model.navigation

import kotlinx.serialization.Serializable

sealed interface MusicNavigationModel {
  @Serializable
  data object Home : MusicNavigationModel
  @Serializable
  data class Category(var name: String) : MusicNavigationModel
}