package com.example.feature.music_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.data.repository.sortMusic
import com.example.core.model.SongSortModel
import com.example.core.model.SongsSortType
import com.example.core.model.SortType
import com.example.core.model.TabBarModel
import com.example.datastore.ArtistSortDataStoreManager
import com.example.datastore.SongsSortDataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
  private var musicSource: MusicSourceImpl,
  private var songsSortDataStoreManager: SongsSortDataStoreManager,
  private var artistSortDataStoreManager: ArtistSortDataStoreManager,
  favoriteMusicSource: FavoriteMusicSourceImpl,
) : ViewModel() {

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  var musicList = combine(
    musicSource.songs(),
    songsSortDataStoreManager.songsSortState,
  ) { songs, sortState ->
    val sorted = sortMusic(
      list = songs,
      isDescending = sortState.isDec,
      songsSortType = sortState.songsSortType,
    )
    isLoading = false
    sorted
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
    emptyList(),
  )

  var songSortModel = songsSortDataStoreManager.songsSortState
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      SongSortModel(isDec = false, songsSortType = SongsSortType.NAME),
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
    emptyList(),
  )

  fun updateSortType(songsSortType: SortType) = viewModelScope.launch {
    songsSortDataStoreManager.updateSongsSortType(songsSortType)
  }

  fun updateSortIsDec(boolean: Boolean) = viewModelScope.launch {
    songsSortDataStoreManager.updateSongsIsDescending(boolean)
  }


}