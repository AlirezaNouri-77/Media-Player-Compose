package com.example.feature.music_artist

import com.example.core.model.MusicModel
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType

data class ArtistScreenUiState(
  val isLoading: Boolean = false,
  val isSortDropDownMenuShow: Boolean = false,
  val sortState: CategorizedSortModel = CategorizedSortModel(sortType = CategorizedSortType.NAME,isDec = false),
  val artistList:List<Pair<String, List<MusicModel>>> = emptyList(),
)