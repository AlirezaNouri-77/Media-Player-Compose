package com.example.core.data.repository

import com.example.core.model.MusicModel
import com.example.core.model.SortType
import com.example.datastore.SongsSortDataStoreManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MusicSource(
  private var musicMediaStoreRepository: MusicRepositoryImpl,
  private var ioDispatcher: CoroutineDispatcher,
  private var songsSortDataStoreManager: SongsSortDataStoreManager,
) : MusicSourceImpl {

  override fun songs(): Flow<List<MusicModel>> = musicMediaStoreRepository
    .getSongs()
    .map {
      val sortState = songsSortDataStoreManager.songsSortState.first()
      sortMusic(
        list = it,
        isDescending = sortState.isDec,
        sortType = sortState.sortType
      )
    }
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

  private suspend inline fun sortMusic(
    list: List<MusicModel>,
    isDescending: Boolean,
    sortType: SortType,
  ): List<MusicModel> {
    return withContext(Dispatchers.Default) {
      when (sortType) {
        SortType.NAME -> if (isDescending) list.sortedByDescending { it.name } else list.sortedBy { it.name }
        SortType.ARTIST -> if (isDescending) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
        SortType.DURATION -> if (isDescending) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
        SortType.SIZE -> if (isDescending) list.sortedByDescending { it.size } else list.sortedBy { it.size }
      }
    }
  }
}
