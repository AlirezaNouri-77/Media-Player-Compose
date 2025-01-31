package com.example.mediaplayerjetpackcompose.data.musicManager

import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import okhttp3.internal.filterList

class SearchMusicManager(
  private var musicSource: MusicSource,
) {

  private var _searchList = MutableStateFlow(emptyList<MusicModel>())
  var searchResult = _searchList.asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun search(input: String) {
    if (input.isNotEmpty() || input.isNotBlank()) {
      val filteredList = musicSource.songs.first().filterList { name.lowercase().contains(input.lowercase()) }
      _searchList.update { filteredList }
    }
  }

}