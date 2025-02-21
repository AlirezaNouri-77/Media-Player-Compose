package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.category

import androidx.compose.runtime.Immutable
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryMusicModel

@Immutable
data class Category(
  val album: List<CategoryMusicModel>,
  val artist: List<CategoryMusicModel>,
  val folder: List<CategoryMusicModel>,
)
