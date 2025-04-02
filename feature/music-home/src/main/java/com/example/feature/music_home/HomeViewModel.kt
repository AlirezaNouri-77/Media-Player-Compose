package com.example.feature.music_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.model.MusicModel
import com.example.core.model.SortState
import com.example.core.model.SortType
import com.example.core.model.TabBarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
  private var musicSource: MusicSourceImpl,
  favoriteMusicSource: FavoriteMusicSourceImpl,
) : ViewModel() {

  init {
    getMusic()
  }

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  private var _sortSate = MutableStateFlow(SortState(SortType.NAME, false))
  var sortState = _sortSate.asStateFlow()

  var musicList = mutableStateListOf<MusicModel>()

  var favoriteSongsMediaId = favoriteMusicSource.favoriteMusicMediaIdList.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var favoriteSongs = favoriteMusicSource.favoriteSongs.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var folder = musicSource.folder().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )


  fun updateSortType(input: SortType) = _sortSate.update { it.copy(sortType = input) }

  fun updateSortIsDec(input: Boolean) = _sortSate.update { it.copy(isDec = input) }

  fun sortMusicListByCategory() {
    viewModelScope.launch {
      val sortedList = sortMusic(list = musicList, isDescending = _sortSate.value.isDec, sortBy = _sortSate.value.sortType)
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

  private suspend inline fun sortMusic(
    list: List<MusicModel>,
    isDescending: Boolean,
    sortBy: SortType,
  ): List<MusicModel> {
    return withContext(Dispatchers.Default) {
      when (sortBy) {
        SortType.NAME -> if (isDescending) list.sortedByDescending{ it.name } else list.sortedBy { it.name }
        SortType.ARTIST -> if (isDescending) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
        SortType.DURATION -> if (isDescending) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
        SortType.SIZE -> if (isDescending) list.sortedByDescending { it.size } else list.sortedBy { it.size }
      }
    }
  }

}