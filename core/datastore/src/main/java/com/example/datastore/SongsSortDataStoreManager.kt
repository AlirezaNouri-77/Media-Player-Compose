package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.model.SongSortModel
import com.example.core.model.SongsSortType
import com.example.core.proto_datastore.SortPreferences
import com.example.core.proto_datastore.Proto_SortType
import com.example.datastore.mapper.toProtoSortType
import com.example.datastore.mapper.toSortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SongsSortDataStoreManager(
  private var dataStore: DataStore<SortPreferences>,
) {

  val songsSortState: Flow<SongSortModel> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(SortPreferences.newBuilder().setSongSortType(Proto_SortType.PROTO_SORT_TYPE_NAME).setSongsIsDescending(true).build())
      } else {
        throw exception
      }
    }.map {
      SongSortModel(
        songsSortType = it.songSortType.toSortType(),
        isDec = it.songsIsDescending,
      )
    }

  suspend fun updateSongsSortType(songsSortType: SongsSortType) {
    dataStore.updateData {
      it.toBuilder().setSongSortType(songsSortType.toProtoSortType()).build()
    }
  }

  suspend fun updateSongsIsDescending(boolean: Boolean) {
    dataStore.updateData {
      it.toBuilder().setSongsIsDescending(boolean).build()
    }
  }

}