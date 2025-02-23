package com.example.mediaplayerjetpackcompose.core.data

import com.example.mediaplayerjetpackcompose.core.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.core.domain.api.FavoriteMusicManagerImpl
import com.example.mediaplayerjetpackcompose.core.model.FavoriteMusic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoriteMusicManager(
  private var favoriteDataBaseDao: DataBaseDao,
  private var dispatcher: CoroutineDispatcher,
) : FavoriteMusicManagerImpl {

  override var favoriteMusicMediaIdList = favoriteDataBaseDao.getFavoriteSongsMediaId()

  // return true if mediaId is not in favorite database
  override suspend fun handleFavoriteSongs(mediaId: String): Boolean {
    return withContext(dispatcher) {
      var isFavorite = favoriteDataBaseDao.isFavorite(mediaId)
      if (isFavorite) {
        favoriteDataBaseDao.deleteFavoriteSong(mediaId)
        return@withContext false
      } else {
        favoriteDataBaseDao.insertFavoriteSong(FavoriteMusic(mediaId = mediaId))
        return@withContext true
      }
    }
  }

}