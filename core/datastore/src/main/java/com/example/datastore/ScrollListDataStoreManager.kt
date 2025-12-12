package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.core.datastore.MyProtoPreferences
import com.example.core.model.datastore.ScrollListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ScrollListDataStoreManager(
    private val dataStore: DataStore<MyProtoPreferences>,
    private val ioDispatcher: CoroutineDispatcher,
) {
    val scrollDataStoreState: Flow<ScrollListState> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(MyProtoPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map {
            ScrollListState(
                homeMusic = it.indexScrollHome,
                artistMusic = it.indexScrollArtist,
                albumMusic = it.indexScrollAlbum,
                folderMusic = it.indexScrollFolder,
                favorite = it.indexScrollFavorite,
            )
        }.flowOn(ioDispatcher)

    suspend fun updateHomeScroll(int: Int) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setIndexScrollHome(int).build()
            }
        }
    }

    suspend fun updateArtistScroll(int: Int) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setIndexScrollArtist(int).build()
            }
        }
    }

    suspend fun updateAlbumScroll(int: Int) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setIndexScrollAlbum(int).build()
            }
        }
    }

    suspend fun updateFavoriteScroll(int: Int) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setIndexScrollFavorite(int).build()
            }
        }
    }

    suspend fun updateFolderScroll(int: Int) {
        withContext(ioDispatcher) {
            dataStore.updateData {
                it.toBuilder().setIndexScrollFolder(int).build()
            }
        }
    }
}
