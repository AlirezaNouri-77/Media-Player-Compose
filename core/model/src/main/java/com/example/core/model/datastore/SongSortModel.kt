package com.example.core.model.datastore

data class SongSortModel(override val sortType: SongsSortType, override val isDec: Boolean) : SortState<SongsSortType>
