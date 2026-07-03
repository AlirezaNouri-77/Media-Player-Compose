package com.shermanrex.feature.music_search

import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.MusicPlayerState

data class SearchScreenUiState(
    val isLoading: Boolean = false,
    val searchList: List<MusicModel> = emptyList(),
    val searchTextFieldValue: String = "",
    val musicPlayerState: MusicPlayerState = MusicPlayerState.Initial,
)
