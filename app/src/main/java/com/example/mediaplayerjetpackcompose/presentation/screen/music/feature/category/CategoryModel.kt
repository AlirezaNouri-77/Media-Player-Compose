package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.category

import androidx.compose.runtime.Immutable
import com.example.mediaplayerjetpackcompose.core.model.CategoryMusic

@Immutable
data class Category(
  val album: List<CategoryMusic>,
  val artist: List<CategoryMusic>,
  val folder: List<CategoryMusic>,
)
