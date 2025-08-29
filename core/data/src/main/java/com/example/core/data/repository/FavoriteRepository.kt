package com.example.core.data.repository

import com.example.core.database.dao.FavoriteDao
import com.example.core.database.model.FavoriteEntity
import com.example.core.domain.respository.FavoriteRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoriteRepository(
    private val favoriteDao: FavoriteDao,
    private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepositoryImpl {
    override suspend fun handleFavoriteSongs(mediaId: String): Boolean {
        return withContext(ioDispatcher) {
            val isFavorite = favoriteDao.isFavorite(mediaId)
            if (isFavorite) {
                favoriteDao.deleteFavoriteSong(mediaId)
                return@withContext false
            } else {
                favoriteDao.insertFavoriteSong(FavoriteEntity(mediaId = mediaId))
                return@withContext true
            }
        }
    }
}
