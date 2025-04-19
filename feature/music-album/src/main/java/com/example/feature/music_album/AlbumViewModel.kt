package com.example.feature.music_album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.data.repository.albumName
import com.example.core.data.util.sortMusic
import com.example.core.model.CategorizedSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.MusicModel
import com.example.datastore.SortDataStoreManagerImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
  private var musicSource: MusicSourceImpl,
  private var albumSortDataStoreManager: SortDataStoreManagerImpl<CategorizedSortModel>,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  val sortState = albumSortDataStoreManager.sortState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    CategorizedSortModel(FolderSortType.NAME, false),
  )

  val album: StateFlow<List<Pair<albumName, List<MusicModel>>>> = combine(
    musicSource.album(),
    albumSortDataStoreManager.sortState,
  ) { songs, sortState ->
    val sortedData = sortMusic(
      list = songs,
      isDescending = sortState.isDec,
      sortType = sortState.sortType
    )
    isLoading = false
    sortedData
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  fun updateSortType(songsSortType: FolderSortType) = viewModelScope.launch {
    albumSortDataStoreManager.updateSortType(songsSortType)
  }

  fun updateSortIsDec(boolean: Boolean) = viewModelScope.launch {
    albumSortDataStoreManager.updateSortOrder(boolean)
  }

}