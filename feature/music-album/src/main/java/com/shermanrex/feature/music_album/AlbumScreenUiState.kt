package com.shermanrex.feature.music_album

import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.datastore.CategorizedSortModel
import com.shermanrex.core.model.datastore.CategorizedSortType

data class AlbumScreenUiState(
    val isLoading: Boolean = false,
    val isSortDropDownMenuShow: Boolean = false,
    val lastScrollState: Int = 0,
    val sortState: CategorizedSortModel = CategorizedSortModel(sortType = CategorizedSortType.NAME, isDec = false),
    val albumList: List<Pair<String, List<MusicModel>>> = emptyList(),
)
