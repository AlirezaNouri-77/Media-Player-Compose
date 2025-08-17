package com.example.feature.music_categorydetail

import com.example.core.model.MusicModel

data class CategoryUiState(
  val isLoading: Boolean = false,
  val songList: List<MusicModel> = emptyList(),
  val gradientColor: Int = 0,
)