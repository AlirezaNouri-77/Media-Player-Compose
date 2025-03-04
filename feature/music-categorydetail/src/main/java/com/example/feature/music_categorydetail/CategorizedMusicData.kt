package com.example.feature.music_categorydetail

import androidx.compose.runtime.Immutable
import com.example.core.model.CategoryMusic

@Immutable
data class CategorizedMusicData(
  val album: List<CategoryMusic>,
  val artist: List<CategoryMusic>,
  val folder: List<CategoryMusic>,
)
