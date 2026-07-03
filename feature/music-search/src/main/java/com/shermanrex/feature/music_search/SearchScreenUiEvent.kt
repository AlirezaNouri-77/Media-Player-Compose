package com.shermanrex.feature.music_search

sealed interface SearchScreenUiEvent {
    data object OnClearSearchTextField : SearchScreenUiEvent

    data class OnSearchTextField(val newValue: String) : SearchScreenUiEvent
}
