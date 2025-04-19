package com.example.core.data.util

import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.MusicModel
import com.example.core.model.datastore.SongsSortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun sortMusic(
  list: List<MusicModel>,
  isDescending: Boolean,
  sortType: SongsSortType,
): List<MusicModel> {
  return withContext(Dispatchers.Default) {
    when (sortType) {
      SongsSortType.NAME -> if (isDescending) list.sortedByDescending { it.name } else list.sortedBy { it.name }
      SongsSortType.ARTIST -> if (isDescending) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
      SongsSortType.DURATION -> if (isDescending) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
      SongsSortType.SIZE -> if (isDescending) list.sortedByDescending { it.size } else list.sortedBy { it.size }
    }
  }
}

suspend inline fun sortMusic(
  list: List<Pair<String, List<MusicModel>>>,
  isDescending: Boolean,
  sortType: CategorizedSortType,
): List<Pair<String, List<MusicModel>>> {
  return withContext(Dispatchers.Default) {
    when (sortType) {
      CategorizedSortType.NAME -> if (isDescending) list.sortedByDescending { it.first } else list.sortedBy { it.first }
      CategorizedSortType.SongsCount -> if (isDescending) list.sortedByDescending { it.second.size } else list.sortedBy { it.second.size }
    }
  }
}

