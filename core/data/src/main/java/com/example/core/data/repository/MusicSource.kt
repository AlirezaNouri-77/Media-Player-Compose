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

  override fun songs(): Flow<List<MusicModel>> = musicMediaStoreRepository
    .getSongs()
    .flowOn(ioDispatcher)

  override fun artist(): Flow<List<Pair<artistName, List<MusicModel>>>> = songs().map {
    it.groupBy { it.artist }.map { it.key to it.value }
  }.flowOn(ioDispatcher)

  override fun album(): Flow<List<Pair<albumName, List<MusicModel>>>> = songs().map {
    it.groupBy { by -> by.album }.map { it.key to it.value }
  }.flowOn(ioDispatcher)


  override fun folder(): Flow<List<Pair<folderName, List<MusicModel>>>> = songs().map {
    it.groupBy { by -> by.folderName }.map { it.key to it.value }
  }.flowOn(ioDispatcher)

}
