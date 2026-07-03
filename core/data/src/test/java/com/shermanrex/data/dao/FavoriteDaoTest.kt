package com.shermanrex.data.dao

import com.shermanrex.core.database.dao.FavoriteDao
import com.shermanrex.core.database.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteDaoTest : FavoriteDao {
    private var favoriteList = mutableListOf<FavoriteEntity>()

    override fun getFavoriteSongsMediaId(): Flow<List<String>> {
        return flow { emit(favoriteList.map { it.mediaId }) }
    }

    override fun insertFavoriteSong(favoriteMediaId: FavoriteEntity) {
        favoriteList.add(favoriteMediaId)
    }

    override fun deleteFavoriteSong(mediaId: String) {
        val index = favoriteList.indexOfFirst { it.mediaId == mediaId }
        favoriteList.removeAt(index)
    }

    override fun isFavorite(mediaId: String): Boolean {
        val isFav = favoriteList.find { it.mediaId == mediaId }
        return isFav != null
    }
}
