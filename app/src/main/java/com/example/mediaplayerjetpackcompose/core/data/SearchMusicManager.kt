package com.example.mediaplayerjetpackcompose.core.data

import com.example.mediaplayerjetpackcompose.core.data.repository.MusicSourceRepository
import com.example.mediaplayerjetpackcompose.core.domain.api.SearchMusicManagerImpl
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import okhttp3.internal.filterList

class SearchMusicManager(
  private var musicSourceRepository: MusicSourceRepository,
) : SearchMusicManagerImpl {

  private var _searchList = MutableStateFlow(emptyList<MusicModel>())
  override var searchList = _searchList.asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun search(input: String) {
    if (input.isNotEmpty() || input.isNotBlank()) {
      val filteredList = musicSourceRepository.songs.first().filterList { name.lowercase().contains(input.lowercase()) }
      _searchList.update { filteredList }
    } else {
      _searchList.update { musicSourceRepository.songs.first() }
    }
  }

}