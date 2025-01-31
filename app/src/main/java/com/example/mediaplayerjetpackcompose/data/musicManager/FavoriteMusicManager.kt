package com.example.mediaplayerjetpackcompose.data.musicManager

import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteMusicManager(
  private var favoriteDataBaseDao: DataBaseDao,
) {

  var favoriteMusicMediaIdList = favoriteDataBaseDao.getFavoriteSongsMediaId()

  // return true if mediaId is not in favorite database
  suspend fun handleFavoriteSongs(mediaId: String): Boolean {
    return withContext(Dispatchers.IO) {
      var isFavorite = favoriteDataBaseDao.isFavorite(mediaId)
      if (isFavorite) {
        favoriteDataBaseDao.deleteFavoriteSong(mediaId)
        return@withContext false
      } else {
        favoriteDataBaseDao.insertFavoriteSong(FavoriteMusicModel(mediaId = mediaId))
        return@withContext true
      }
    }
  }

}