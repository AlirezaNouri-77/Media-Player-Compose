package com.example.mediaplayerjetpackcompose.data.repository

import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlin.collections.map

class MusicSource(
  musicMediaStoreRepository: MusicMediaStoreRepository,
  private var favoriteDataBaseDao: DataBaseDao,
) {

  var songs = musicMediaStoreRepository
    .getMedia()

  fun favoriteSongs(): Flow<List<MusicModel>> =
    combine(songs, favoriteDataBaseDao.getFavoriteSongsMediaId()) { musicList, favoriteMediaIdList ->
      musicList.filterIndexed { index, item ->
        item.musicId.toString() in favoriteMediaIdList
      }
    }

  fun artist(): Flow<List<CategoryMusicModel>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.artist }.map { CategoryMusicModel(it.key, it.value) })
    }
  }

  fun album(): Flow<List<CategoryMusicModel>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.album }.map { CategoryMusicModel(it.key, it.value) })
    }
  }

  fun folder(): Flow<List<CategoryMusicModel>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.folderName }.map { CategoryMusicModel(it.key, it.value) })
    }
  }


}


