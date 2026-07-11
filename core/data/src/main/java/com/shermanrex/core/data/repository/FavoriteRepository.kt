package com.shermanrex.core.data.repository

import com.shermanrex.core.database.dao.FavoriteDao
import com.shermanrex.core.database.model.FavoriteEntity
import com.shermanrex.core.domain.respository.FavoriteRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavoriteRepository(
    private val favoriteDao: FavoriteDao,
    private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepositoryImpl {
    override fun favoritesMediaIdList(): Flow<List<String>> {
        return favoriteDao.getFavoriteSongsMediaId()
    }

    override suspend fun handleFavoriteSongs(mediaId: String): Boolean {
        return withContext(ioDispatcher) {
            val isFavorite = favoriteDao.isFavorite(mediaId)
            if (isFavorite) {
                favoriteDao.deleteFavoriteSong(mediaId)
            } else {
                favoriteDao.insertFavoriteSong(FavoriteEntity(mediaId = mediaId))
            }
            favoriteDao.isFavorite(mediaId)
        }
    }
}
