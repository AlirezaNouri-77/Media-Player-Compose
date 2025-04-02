package com.example.core.data.repository

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface FavoriteMusicSourceImpl {
  var favoriteMusicMediaIdList: Flow<List<String>>
  var favoriteSongs: Flow<List<MusicModel>>
  suspend fun handleFavoriteSongs(mediaId: String): Boolean
}