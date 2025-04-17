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

class ArtistSortDataStoreManager(
  private val dataStore: DataStore<SortPreferences>,
) {

  val artistSortState: Flow<FolderSortModel> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(SortPreferences.newBuilder().setArtistSortType(Proto_DataStore_Folder.Name).setSongsIsDescending(true).build())
      } else {
        throw exception
      }
    }.map {
      FolderSortModel(
        sortType = it.artistSortType.toFolderSortType(),
        isDec = it.artistIsDescending,
      )
    }

  suspend fun updateFolderSortType(sortType: FolderSortType) {
    dataStore.updateData {
      it.toBuilder().setArtistSortType(sortType.toProtoSortType()).build()
    }
  }

  suspend fun updateArtistIsDescending(boolean: Boolean) {
    dataStore.updateData {
      it.toBuilder().setArtistIsDescending(boolean).build()
    }
  }

}