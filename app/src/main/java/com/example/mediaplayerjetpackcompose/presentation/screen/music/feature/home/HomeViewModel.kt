package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.core.data.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.core.data.repository.MusicSourceRepository
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import com.example.mediaplayerjetpackcompose.core.model.SortListType
import com.example.mediaplayerjetpackcompose.core.model.TabBarModel
import com.example.mediaplayerjetpackcompose.core.model.SortState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
  private var musicSourceRepository: MusicSourceRepository,
  favoriteMusicManager: FavoriteMusicManager,
) : ViewModel() {

  init {
    getMusic()
  }

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  private var _sortSate = MutableStateFlow(SortState(SortListType.NAME, false))
  var sortState = _sortSate.asStateFlow()

  var musicList = mutableStateListOf<MusicModel>()

  var favoriteSongsMediaId = favoriteMusicManager.favoriteMusicMediaIdList.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var favoriteSongs = musicSourceRepository.favoriteSongs().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var folder = musicSourceRepository.folder().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )


  fun updateSortType(input: SortListType) = _sortSate.update { it.copy(sortType = input) }

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
    musicSourceRepository.songs.collect { result ->
      musicList = result.toMutableStateList()
      isLoading = false
    }
  }

  private suspend inline fun sortMusic(
    list: List<MusicModel>,
    isDescending: Boolean,
    sortBy: SortListType,
  ): List<MusicModel> {
    return withContext(Dispatchers.Default) {
      when (sortBy) {
        SortListType.NAME -> if (isDescending) list.sortedByDescending{ it.name } else list.sortedBy { it.name }
        SortListType.ARTIST -> if (isDescending) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
        SortListType.DURATION -> if (isDescending) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
        SortListType.SIZE -> if (isDescending) list.sortedByDescending { it.size } else list.sortedBy { it.size }
      }
    }
  }

}