package com.example.mediaplayerjetpackcompose.core.data.repository

import com.example.mediaplayerjetpackcompose.core.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.core.domain.api.MusicRepositoryImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.MusicSourceImpl
import com.example.mediaplayerjetpackcompose.core.model.CategoryMusic
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlin.collections.map

class MusicSourceRepository(
  musicMediaStoreRepository: MusicRepositoryImpl,
  private var favoriteDataBaseDao: DataBaseDao,
) : MusicSourceImpl {

  override var songs = musicMediaStoreRepository
    .getSongs()

  override fun favoriteSongs(): Flow<List<MusicModel>> =
    combine(songs, favoriteDataBaseDao.getFavoriteSongsMediaId()) { musicList, favoriteMediaIdList ->
      musicList.filterIndexed { index, item ->
        item.musicId.toString() in favoriteMediaIdList
      }
    }

  override fun artist(): Flow<List<CategoryMusic>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.artist }.map { CategoryMusic(it.key, it.value) })
    }
  }

  override fun album(): Flow<List<CategoryMusic>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.album }.map { CategoryMusic(it.key, it.value) })
    }
  }

  override fun folder(): Flow<List<CategoryMusic>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.folderName }.map { CategoryMusic(it.key, it.value) })
    }
  }


}


