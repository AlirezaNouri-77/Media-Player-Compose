package com.example.core.data.util

import com.example.core.data.repository.MusicSourceImpl
import com.example.core.domain.api.SearchMusicManagerImpl
import com.example.core.model.MusicModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SearchMusicManager(
  private var musicSource: MusicSourceImpl,
  private var ioDispatcher: CoroutineDispatcher,
) : SearchMusicManagerImpl {

  private var _searchList = MutableStateFlow(emptyList<MusicModel>())
  override var searchList = _searchList.asStateFlow()

  override suspend fun search(input: String) {
    withContext(ioDispatcher) {
      if (input.isNotEmpty() || input.isNotBlank()) {
        val filteredList = musicSource.songs.first().filter { it.name.lowercase().contains(input.lowercase()) }
        _searchList.update { filteredList }
      } else {
        _searchList.update { musicSource.songs.first() }
      }
    }
  }

}