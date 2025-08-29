package com.example.feature.music_artist

import com.example.core.model.datastore.CategorizedSortType

sealed interface ArtistUiEvent {
    object HideSortDropDownMenu : ArtistUiEvent

    object ShowSortDropDownMenu : ArtistUiEvent

    data class UpdateSortOrder(val isDec: Boolean) : ArtistUiEvent

    data class UpdateSortType(val sortType: CategorizedSortType) : ArtistUiEvent
}
