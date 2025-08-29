package com.example.core.domain.respository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepositoryImpl {
    fun favoritesMediaIdList(): Flow<List<String>>

    suspend fun handleFavoriteSongs(mediaId: String): Boolean
}
