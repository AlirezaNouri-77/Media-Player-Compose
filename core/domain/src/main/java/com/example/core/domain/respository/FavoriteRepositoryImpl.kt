package com.example.core.domain.respository

interface FavoriteRepositoryImpl {
    suspend fun handleFavoriteSongs(mediaId: String): Boolean
}
