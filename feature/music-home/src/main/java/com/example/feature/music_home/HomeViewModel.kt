package com.example.feature.music_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.data.repository.sortMusic
import com.example.core.model.FolderSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.SongSortModel
import com.example.core.model.SongsSortType
import com.example.core.model.SortType
import com.example.core.model.TabBarModel
import com.example.datastore.FolderSortDataStoreManager
import com.example.datastore.SongsSortDataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
  private val musicSource: MusicSourceImpl,
  private val songsSortDataStoreManager: SongsSortDataStoreManager,
  private val folderSortDataStoreManager: FolderSortDataStoreManager,
  favoriteMusicSource: FavoriteMusicSourceImpl,
) : ViewModel() {

  var isLoading by mutableStateOf(true)
    private set

  var songsList = combine(
    musicSource.songs(),
    songsSortDataStoreManager.songsSortState,
  ) { songs, sortState ->
    val sorted = sortMusic(
      list = songs,
      isDescending = sortState.isDec,
      sortType = sortState.songsSortType,
    )
    isLoading = false
    sorted
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
    emptyList(),
  )

  var folderSongsData = combine(
    musicSource.folder(),
    folderSortDataStoreManager.folderSortState,
  ) { songs, sortState ->
    val sorted = sortMusic(
      list = songs,
      isDescending = sortState.isDec,
      sortType = sortState.sortType,
    )
    sorted
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
    emptyList(),
  )

  var songSortState = songsSortDataStoreManager.songsSortState
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      SongSortModel(isDec = false, songsSortType = SongsSortType.NAME),
    )

  var folderSortState = folderSortDataStoreManager.folderSortState
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      FolderSortModel(isDec = false, sortType = FolderSortType.NAME),
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

  fun updateDataStoreSortType(tabBarPosition: TabBarModel, sortType: SortType) = viewModelScope.launch {
    if (tabBarPosition == TabBarModel.All) {
      songsSortDataStoreManager.updateSongsSortType(sortType)
    } else if (tabBarPosition == TabBarModel.Folder) {
      folderSortDataStoreManager.updateSortType(sortType)
    }
  }

  fun updateDataStoreOrder(tabBarPosition: TabBarModel) = viewModelScope.launch {
    if (tabBarPosition == TabBarModel.All) {
      songsSortDataStoreManager.updateSongsIsDescending(!songSortState.value.isDec)
    } else if (tabBarPosition == TabBarModel.Folder) {
      folderSortDataStoreManager.updateOrder(!folderSortState.value.isDec)
    }
  }

}
