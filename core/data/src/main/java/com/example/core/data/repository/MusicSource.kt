package com.example.core.data.repository

import com.example.core.model.MusicModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MusicSource(
  private var musicMediaStoreRepository: MusicRepositoryImpl,
  private var ioDispatcher: CoroutineDispatcher,
) : MusicSourceImpl {

  override var songs = musicMediaStoreRepository
    .getSongs()
    .flowOn(ioDispatcher)

  override fun artist(): Flow<Map<artistName, List<MusicModel>>> = musicMediaStoreRepository.getSongs().map {
    it.groupBy { by -> by.artist }
  }.flowOn(ioDispatcher)

  override fun album(): Flow<Map<albumName, List<MusicModel>>> = musicMediaStoreRepository.getSongs().map {
    it.groupBy { by -> by.album }
  }.flowOn(ioDispatcher)


  override fun folder(): Flow<Map<folderName, List<MusicModel>>> = musicMediaStoreRepository.getSongs().map {
    it.groupBy { by -> by.folderName }
  }.flowOn(ioDispatcher)

}


