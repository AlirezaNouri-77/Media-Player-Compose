package com.example.feature.music_album

import com.example.core.model.datastore.CategorizedSortType

sealed interface AlbumUiEvent {
    object HideSortDropDownMenu : AlbumUiEvent

    object ShowSortDropDownMenu : AlbumUiEvent

    data class UpdateSortOrder(val isDec: Boolean) : AlbumUiEvent

    data class UpdateSortType(val sortType: CategorizedSortType) : AlbumUiEvent

    data class UpdateScrollIndex(val index: Int) : AlbumUiEvent
}
