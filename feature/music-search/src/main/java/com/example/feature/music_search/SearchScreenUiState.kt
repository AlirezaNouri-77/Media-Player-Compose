package com.example.feature.music_search

import com.example.core.model.MusicModel

data class SearchScreenUiState(
    val isLoading: Boolean = false,
    val searchList: List<MusicModel> = emptyList(),
    val searchTextFieldValue: String = "",
)
