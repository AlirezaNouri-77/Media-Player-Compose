package com.example.datastore

import com.example.core.model.datastore.SortType
import kotlinx.coroutines.flow.Flow

interface SortDataStoreManagerImpl<T> {
    val sortState: Flow<T>

    suspend fun updateSortType(sortType: SortType)

    suspend fun updateSortOrder(boolean: Boolean)
}
