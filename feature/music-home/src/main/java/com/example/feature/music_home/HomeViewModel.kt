package com.example.feature.music_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.model.MusicModel
import com.example.core.model.SongSortModel
import com.example.core.model.SortType
import com.example.core.model.TabBarModel
import com.example.datastore.SongsSortDataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
  private var musicSource: MusicSourceImpl,
  private var songsSortDataStoreManager: SongsSortDataStoreManager,
  favoriteMusicSource: FavoriteMusicSourceImpl,
) : ViewModel() {

  init {
    getMusic()
  }

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  private var _musicList = MutableStateFlow(emptyList<MusicModel>())
  var musicList = _musicList.asStateFlow()

  var songSortModel = songsSortDataStoreManager.songsSortState
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      SongSortModel(isDec = false, sortType = SortType.NAME),
    )

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
    emptyMap(),
  )

  fun updateSortType(sortType: SortType) = viewModelScope.launch {
    songsSortDataStoreManager.updateSongsSortType(sortType)
    sortMusicListByCategory()
  }

  fun updateSortIsDec(boolean: Boolean) = viewModelScope.launch {
    songsSortDataStoreManager.updateSongsIsDescending(boolean)
    sortMusicListByCategory()
  }

  fun sortMusicListByCategory() {
    viewModelScope.launch {
      val sortedList = musicSource.songs().first()
      _musicList.value = sortedList
    }
  }

  private fun getMusic() = viewModelScope.launch {
    musicSource.songs().collect {
      _musicList.value = it
      isLoading = false
    }
  }


}