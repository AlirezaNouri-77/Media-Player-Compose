package com.example.feature.music_search

import com.example.core.model.MusicModel
import com.example.core.model.MusicPlayerState

data class SearchScreenUiState(
    val isLoading: Boolean = false,
    val searchList: List<MusicModel> = emptyList(),
    val searchTextFieldValue: String = "",
    val musicPlayerState: MusicPlayerState = MusicPlayerState.Initial,
)
