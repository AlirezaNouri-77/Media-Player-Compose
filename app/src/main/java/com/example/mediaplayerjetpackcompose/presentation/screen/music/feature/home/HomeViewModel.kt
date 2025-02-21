package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.musicManager.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import com.example.mediaplayerjetpackcompose.data.util.sortMusic
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
  private var musicSource: MusicSource,
  favoriteMusicManager: FavoriteMusicManager,
) : ViewModel() {

  init {
    getMusic()
  }

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  private var _sortSate = MutableStateFlow(SortState(SortTypeModel.NAME, false))
  var sortState = _sortSate.asStateFlow()

  var musicList = mutableStateListOf<MusicModel>()

  var favoriteSongsMediaId = favoriteMusicManager.favoriteMusicMediaIdList.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var favoriteSongs = musicSource.favoriteSongs().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var folder = musicSource.folder().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )


  fun updateSortType(input: SortTypeModel) = _sortSate.update { it.copy(sortType = input) }

  fun updateSortIsDec(input: Boolean) = _sortSate.update { it.copy(isDec = input) }

  fun sortMusicListByCategory() {
    viewModelScope.launch {
      var sortedList = sortMusic(list = musicList, isDescending = _sortSate.value.isDec, sortBy = _sortSate.value.sortType)
      musicList.apply {
        clear()
        addAll(sortedList)
      }
    }
  }


  private fun getMusic() = viewModelScope.launch {
    musicSource.songs.collect { result ->
      musicList = result.toMutableStateList()
      isLoading = false
    }
  }

}