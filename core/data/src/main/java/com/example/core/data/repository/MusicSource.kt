package com.example.core.data.repository

import com.example.core.domain.api.MusicRepositoryImpl
import com.example.core.domain.api.MusicSourceImpl
import com.example.core.model.CategoryMusic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn

class MusicSource(
  musicMediaStoreRepository: MusicRepositoryImpl,
  private var ioDispatcher: CoroutineDispatcher,
) : MusicSourceImpl {

  override var songs = musicMediaStoreRepository
    .getSongs()
    .flowOn(ioDispatcher)

  override fun artist(): Flow<List<CategoryMusic>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.artist }.map { CategoryMusic(it.key, it.value) })
    }
  }.flowOn(ioDispatcher)

  override fun album(): Flow<List<CategoryMusic>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.album }.map { CategoryMusic(it.key, it.value) })
    }
  }.flowOn(ioDispatcher)

  override fun folder(): Flow<List<CategoryMusic>> = channelFlow {
    songs.collectLatest { musicList ->
      send(musicList.groupBy { by -> by.folderName }.map { CategoryMusic(it.key, it.value) })
    }
  }.flowOn(ioDispatcher)


}


