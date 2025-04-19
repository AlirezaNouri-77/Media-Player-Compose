package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.model.CategorizedSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.SortType
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

class AlbumSortDataStoreManager(
  private val dataStore: DataStore<SortPreferences>,
  private val ioDispatcher: CoroutineDispatcher,
) : SortDataStoreManagerImpl<CategorizedSortModel> {

  override val sortState: Flow<CategorizedSortModel> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        emit(SortPreferences.newBuilder().setAlbumSortType(Proto_DataStore_Folder.Name).setAlbumIsDescending(true).build())
      } else {
        throw exception
      }
    }.map {
      CategorizedSortModel(
        sortType = it.albumSortType.toFolderSortType(),
        isDec = it.albumIsDescending,
      )
    }.flowOn(ioDispatcher)

  override suspend fun updateSortType(sortType: SortType) {
    withContext(ioDispatcher) {
      dataStore.updateData {
        it.toBuilder().setAlbumSortType((sortType as FolderSortType).toProtoSortType()).build()
      }
    }
  }

  override suspend fun updateSortOrder(boolean: Boolean) {
    withContext(ioDispatcher) {
      dataStore.updateData {
        it.toBuilder().setAlbumIsDescending(boolean).build()
      }
    }
  }

}