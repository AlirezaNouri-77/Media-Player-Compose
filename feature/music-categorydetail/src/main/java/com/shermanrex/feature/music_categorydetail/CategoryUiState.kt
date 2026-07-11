package com.shermanrex.feature.music_categorydetail

import androidx.compose.runtime.Stable
import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.PlayingMusicState

@Stable
data class CategoryUiState(
    val isLoading: Boolean = false,
    val songList: List<MusicModel> = emptyList(),
    val detail: String = "",
    val thumbnailDominateColor: Int = 0,
    val playingMusicState: PlayingMusicState = PlayingMusicState.Initial,
)
