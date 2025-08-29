package com.example.feature.music_album

import com.example.core.model.MusicModel
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType

data class AlbumScreenUiState(
    val isLoading: Boolean = false,
    val isSortDropDownMenuShow: Boolean = false,
    val sortState: CategorizedSortModel = CategorizedSortModel(sortType = CategorizedSortType.NAME, isDec = false),
    val albumList: List<Pair<String, List<MusicModel>>> = emptyList(),
)
