package com.example.feature.music_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.data.util.sortMusic
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SongSortModel
import com.example.core.model.datastore.SongsSortType
import com.example.core.model.datastore.SortType
import com.example.core.model.TabBarModel
import com.example.datastore.SortDataStoreManagerImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
  private val musicSource: MusicSourceImpl,
  private val songsSortDataStoreManager: SortDataStoreManagerImpl<SongSortModel>,
  private val folderSortDataStoreManager: SortDataStoreManagerImpl<CategorizedSortModel>,
  favoriteMusicSource: FavoriteRepositoryImpl,
) : ViewModel() {

  var isLoading by mutableStateOf(true)
    private set

  var songsList = combine(
    musicSource.songs(),
    songsSortDataStoreManager.sortState,
  ) { songs, sortState ->
    val sorted = sortMusic(
      list = songs,
      isDescending = sortState.isDec,
      sortType = sortState.sortType,
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
    folderSortDataStoreManager.sortState,
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

  var songSortState = songsSortDataStoreManager.sortState
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      SongSortModel(isDec = false, sortType = SongsSortType.NAME),
    )

  var folderSortState = folderSortDataStoreManager.sortState
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      CategorizedSortModel(isDec = false, sortType = CategorizedSortType.NAME),
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
      songsSortDataStoreManager.updateSortType(sortType)
    } else if (tabBarPosition == TabBarModel.Folder) {
      folderSortDataStoreManager.updateSortType(sortType)
    }
  }

  fun updateDataStoreOrder(tabBarPosition: TabBarModel) = viewModelScope.launch {
    if (tabBarPosition == TabBarModel.All) {
      songsSortDataStoreManager.updateSortOrder(!songSortState.value.isDec)
    } else if (tabBarPosition == TabBarModel.Folder) {
      folderSortDataStoreManager.updateSortOrder(!folderSortState.value.isDec)
    }
  }

}
