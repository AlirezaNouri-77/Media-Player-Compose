package com.example.core.model.datastore

data class CategorizedSortModel(override val sortType: CategorizedSortType, override val isDec: Boolean) : SortState<CategorizedSortType>
