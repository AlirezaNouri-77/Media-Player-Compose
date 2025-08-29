package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.model.datastore.SongSortModel
import com.example.core.model.datastore.SongsSortType
import com.example.core.model.datastore.SortType
import com.example.core.proto_datastore.Proto_SortType
import com.example.core.proto_datastore.SortPreferences
import com.example.datastore.mapper.toProtoSortType
import com.example.datastore.mapper.toSortType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SongsSortDataStoreManager(
    private val dataStore: DataStore<SortPreferences>,
    private val ioDispatcher: CoroutineDispatcher,
) : SortDataStoreManagerImpl<SongSortModel> {
    override val sortState: Flow<SongSortModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(SortPreferences.newBuilder().setSongSortType(Proto_SortType.PROTO_SORT_TYPE_NAME).setSongsIsDescending(true).build())
            } else {
                throw exception
            }
        }.map {
            SongSortModel(
                sortType = it.songSortType.toSortType(),
                isDec = it.songsIsDescending,
            )
        }.flowOn(ioDispatcher)

    override suspend fun updateSortType(sortType: SortType) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setSongSortType((sortType as SongsSortType).toProtoSortType()).build()
            }
        }
    }

    override suspend fun updateSortOrder(boolean: Boolean) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setSongsIsDescending(boolean).build()
            }
        }
    }
}
