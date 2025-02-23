package com.example.mediaplayerjetpackcompose.core.domain.api

import kotlinx.coroutines.flow.Flow

interface FavoriteMusicManagerImpl {
  var favoriteMusicMediaIdList: Flow<List<String>>
  suspend fun handleFavoriteSongs(mediaId: String): Boolean
}