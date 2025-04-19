package com.example.core.model

interface SortStateModel<T : SortType> {
  val sortType: T
  val isDec: Boolean
}

data class SongSortModel(override val sortType: SongsSortType, override val isDec: Boolean) : SortStateModel<SongsSortType>

data class CategorizedSortModel(override val sortType: FolderSortType, override val isDec: Boolean) : SortStateModel<FolderSortType>