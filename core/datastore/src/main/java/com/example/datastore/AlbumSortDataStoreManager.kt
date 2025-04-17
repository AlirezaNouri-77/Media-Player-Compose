package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.model.FolderSortModel
import com.example.core.model.FolderSortType
import com.example.core.proto_datastore.Proto_DataStore_Folder
import com.example.core.proto_datastore.SortPreferences
import com.example.datastore.mapper.toFolderSortType
import com.example.datastore.mapper.toProtoSortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AlbumSortDataStoreManager(
  private var dataStore: DataStore<SortPreferences>,
) {

  val albumSortState: Flow<FolderSortModel> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(SortPreferences.newBuilder().setAlbumSortType(Proto_DataStore_Folder.Name).setAlbumIsDescending(true).build())
      } else {
        throw exception
      }
    }.map {
      FolderSortModel(
        sortType = it.albumSortType.toFolderSortType(),
        isDec = it.albumIsDescending,
      )
    }

  suspend fun updateSortType(songsSortType: FolderSortType) {
    dataStore.updateData {
      it.toBuilder().setAlbumSortType(songsSortType.toProtoSortType()).build()
    }
  }

  suspend fun updateSortOrder(boolean: Boolean) {
    dataStore.updateData {
      it.toBuilder().setAlbumIsDescending(boolean).build()
    }
  }

}