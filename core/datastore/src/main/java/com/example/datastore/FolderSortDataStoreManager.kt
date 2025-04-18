package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.model.FolderSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.SortType
import com.example.core.proto_datastore.Proto_DataStore_Folder
import com.example.core.proto_datastore.SortPreferences
import com.example.datastore.mapper.toFolderSortType
import com.example.datastore.mapper.toProtoSortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FolderSortDataStoreManager(
  private val dataStore: DataStore<SortPreferences>,
) {

  val folderSortState: Flow<FolderSortModel> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(SortPreferences.newBuilder().setFolderSortType(Proto_DataStore_Folder.Name).setFolderIsDescending(true).build())
      } else {
        throw exception
      }
    }.map {
      FolderSortModel(
        sortType = it.folderSortType.toFolderSortType(),
        isDec = it.folderIsDescending,
      )
    }

  suspend fun updateSortType(sortType: SortType) {
    dataStore.updateData {
      it.toBuilder().setFolderSortType((sortType as FolderSortType).toProtoSortType()).build()
    }
  }

  suspend fun updateOrder(boolean: Boolean) {
    dataStore.updateData {
      it.toBuilder().setFolderIsDescending(boolean).build()
    }
  }

}