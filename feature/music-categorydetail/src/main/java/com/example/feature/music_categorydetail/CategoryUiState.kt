package com.example.feature.music_categorydetail

import androidx.compose.runtime.Stable
import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel

@Stable
data class CategoryUiState(
    val isLoading: Boolean = false,
    val songList: List<MusicModel> = emptyList(),
    val detail: String = "",
    val thumbnailDominateColor: Int = 0,
    val musicPlayerState: PlayerStateModel = PlayerStateModel.Initial,
)
