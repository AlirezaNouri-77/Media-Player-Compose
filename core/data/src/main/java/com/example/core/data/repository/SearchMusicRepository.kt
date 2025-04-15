package com.example.core.data.repository

import com.example.core.model.MusicModel
import com.example.core.model.SortType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SearchMusicRepository(
  private var musicSource: MusicSourceImpl,
  private var ioDispatcher: CoroutineDispatcher,
) : SearchMusicRepositoryImpl {

  private var _searchList = MutableStateFlow(emptyList<MusicModel>())
  override var searchList = _searchList.asStateFlow()

  override suspend fun search(input: String) {
    withContext(ioDispatcher) {
      if (input.isNotEmpty() || input.isNotBlank()) {
        val filteredList = musicSource.songs().first().filter { it.name.lowercase().contains(input.lowercase()) }
        _searchList.update { filteredList }
      } else {
        _searchList.update { musicSource.songs().first() }
      }
    }
  }

}