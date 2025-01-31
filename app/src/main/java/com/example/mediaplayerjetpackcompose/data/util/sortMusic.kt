package com.example.mediaplayerjetpackcompose.data.util;

import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun sortMusic(
  list: List<MusicModel>,
  isDescending: Boolean,
  sortBy: SortTypeModel,
): List<MusicModel> {
  return withContext(Dispatchers.Default) {
    when (sortBy) {
      SortTypeModel.NAME -> if (isDescending) list.sortedByDescending{ it.name } else list.sortedBy { it.name }
      SortTypeModel.ARTIST -> if (isDescending) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
      SortTypeModel.DURATION -> if (isDescending) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
      SortTypeModel.SIZE -> if (isDescending) list.sortedByDescending { it.size } else list.sortedBy { it.size }
    }
  }
}