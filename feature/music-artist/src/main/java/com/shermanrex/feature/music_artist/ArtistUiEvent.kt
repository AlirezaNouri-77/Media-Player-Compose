package com.shermanrex.feature.music_artist

import com.shermanrex.core.model.datastore.CategorizedSortType

sealed interface ArtistUiEvent {
    object HideSortDropDownMenu : ArtistUiEvent

    object ShowSortDropDownMenu : ArtistUiEvent

    data class UpdateSortOrder(val isDec: Boolean) : ArtistUiEvent

    data class UpdateSortType(val sortType: CategorizedSortType) : ArtistUiEvent

    data class UpdateScrollIndex(val index: Int) : ArtistUiEvent
}
