package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SortType
import com.example.core.proto_datastore.Proto_DataStore_Folder
import com.example.core.proto_datastore.SortPreferences
import com.example.datastore.mapper.toFolderSortType
import com.example.datastore.mapper.toProtoSortType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FolderSortDataStoreManager(
  private val dataStore: DataStore<SortPreferences>,
  private val ioDispatcher: CoroutineDispatcher,
) : SortDataStoreManagerImpl<CategorizedSortModel> {

  override val sortState: Flow<CategorizedSortModel> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(SortPreferences.newBuilder().setFolderSortType(Proto_DataStore_Folder.Name).setFolderIsDescending(true).build())
      } else {
        throw exception
      }
    }.map {
      CategorizedSortModel(
        sortType = it.folderSortType.toFolderSortType(),
        isDec = it.folderIsDescending,
      )
    }.flowOn(ioDispatcher)

  override suspend fun updateSortType(sortType: SortType) {
    withContext(ioDispatcher) {
      dataStore.updateData {
        it.toBuilder().setFolderSortType((sortType as CategorizedSortType).toProtoSortType()).build()
      }
    }
  }

  override suspend fun updateSortOrder(boolean: Boolean) {
    withContext(ioDispatcher) {
      dataStore.updateData {
        it.toBuilder().setFolderIsDescending(boolean).build()
      }
    }
  }

}