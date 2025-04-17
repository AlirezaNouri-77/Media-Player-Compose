package com.example.core.data.repository

import com.example.core.model.FolderSortType
import com.example.core.model.MusicModel
import com.example.core.model.SongsSortType
import com.example.datastore.ArtistSortDataStoreManager
import com.example.datastore.SongsSortDataStoreManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.SortedMap

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

  override fun album(): Flow<Map<albumName, List<MusicModel>>> = songs().map {
    it.groupBy { by -> by.album }
  }.flowOn(ioDispatcher)


  override fun folder(): Flow<Map<folderName, List<MusicModel>>> = songs().map {
    it.groupBy { by -> by.folderName }
  }.flowOn(ioDispatcher)

}

suspend inline fun sortMusic(
  list: List<MusicModel>,
  isDescending: Boolean,
  songsSortType: SongsSortType,
): List<MusicModel> {
  return withContext(Dispatchers.Default) {
    when (songsSortType) {
      SongsSortType.NAME -> if (isDescending) list.sortedByDescending { it.name } else list.sortedBy { it.name }
      SongsSortType.ARTIST -> if (isDescending) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
      SongsSortType.DURATION -> if (isDescending) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
      SongsSortType.SIZE -> if (isDescending) list.sortedByDescending { it.size } else list.sortedBy { it.size }
    }
  }
}

suspend inline fun sortMapSort2(
  data: List<Pair<String, List<MusicModel>>>,
  isDescending: Boolean,
  sortType: FolderSortType,
): List<Pair<String, List<MusicModel>>> {
  return withContext(Dispatchers.Default) {
    when (sortType) {
      FolderSortType.NAME -> if (isDescending) data.sortedByDescending { it.first } else data.sortedBy { it.first }
      FolderSortType.SongsCount -> if (isDescending) data.sortedByDescending { it.second.size } else data.sortedBy { it.second.size }
    }
  }
}
