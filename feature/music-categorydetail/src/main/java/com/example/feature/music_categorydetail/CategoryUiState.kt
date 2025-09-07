package com.example.feature.music_categorydetail

import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel

data class CategoryUiState(
    val isLoading: Boolean = false,
    val songList: List<MusicModel> = emptyList(),
    val detail: String = "",
    val thumbnailDominateColor: Int = 0,
    val musicPlayerState: PlayerStateModel = PlayerStateModel.Initial,
)
