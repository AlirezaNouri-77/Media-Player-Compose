package com.shermanrex.feature.music_artist

import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.datastore.CategorizedSortModel
import com.shermanrex.core.model.datastore.CategorizedSortType

data class ArtistScreenUiState(
    val isLoading: Boolean = false,
    val isSortDropDownMenuShow: Boolean = false,
    val isPlayerHasMediaItem: Boolean = false,
    val lastScrollIndex: Int = 0,
    val sortState: CategorizedSortModel = CategorizedSortModel(sortType = CategorizedSortType.NAME, isDec = false),
    val artistList: List<Pair<String, List<MusicModel>>> = emptyList(),
)
