package com.example.core.data.repository

import com.example.core.database.dao.FavoriteDao
import com.example.core.database.model.FavoriteEntity
import com.example.core.domain.api.FavoriteMusicSourceImpl
import com.example.core.model.MusicModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoriteMusicSource(
  private val favoriteFavoriteDao: FavoriteDao,
  private val musicSource: MusicSource,
  private val ioDispatcher: CoroutineDispatcher,
) : FavoriteMusicSourceImpl {

  override var favoriteMusicMediaIdList = favoriteFavoriteDao.getFavoriteSongsMediaId().flowOn(ioDispatcher)

  override var favoriteSongs: Flow<List<MusicModel>> =
    combine(musicSource.songs, favoriteFavoriteDao.getFavoriteSongsMediaId()) { musicList, favoriteMediaIdList ->
      musicList.filterIndexed { index, item ->
        item.musicId.toString() in favoriteMediaIdList
      }
    }.flowOn(ioDispatcher)

  // return true if mediaId is not in favorite database
  override suspend fun handleFavoriteSongs(mediaId: String): Boolean {
    return withContext(ioDispatcher) {
      var isFavorite = favoriteFavoriteDao.isFavorite(mediaId)
      if (isFavorite) {
        favoriteFavoriteDao.deleteFavoriteSong(mediaId)
        return@withContext false
      } else {
        favoriteFavoriteDao.insertFavoriteSong(FavoriteEntity(mediaId = mediaId))
        return@withContext true
      }
    }
  }

}