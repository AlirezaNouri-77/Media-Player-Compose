package com.shermanrex.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.datastore.MyProtoPreferences
import com.example.core.datastore.Proto_DataStore_Folder
import com.shermanrex.core.model.datastore.CategorizedSortModel
import com.shermanrex.core.model.datastore.CategorizedSortType
import com.shermanrex.core.model.datastore.SortType
import com.shermanrex.datastore.mapper.toFolderSortType
import com.shermanrex.datastore.mapper.toProtoSortType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FolderSortDataStoreManager(
    private val dataStore: DataStore<MyProtoPreferences>,
    private val ioDispatcher: CoroutineDispatcher,
) : SortDataStoreManagerImpl<CategorizedSortModel> {
    override val sortState: Flow<CategorizedSortModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(MyProtoPreferences.newBuilder().setFolderSortType(Proto_DataStore_Folder.NAME).setFolderIsDescending(true).build())
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
