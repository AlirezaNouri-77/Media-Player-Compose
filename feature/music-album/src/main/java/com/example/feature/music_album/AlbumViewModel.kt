package com.example.feature.music_album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.util.sortMusic
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.datastore.SortDataStoreManagerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumViewModel(
  private var musicSource: MusicSourceImpl,
  private var albumSortDataStoreManager: SortDataStoreManagerImpl<CategorizedSortModel>,
) : ViewModel() {

  private var mUiState = MutableStateFlow(AlbumScreenUiState())
  val albumScreenUiState = mUiState
    .onStart {
      getAlbumData()
      getSortState()
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      AlbumScreenUiState(),
    )

  fun onEvent(event: AlbumUiEvent) {
    when (event) {
      AlbumUiEvent.HideSortDropDownMenu -> mUiState.update { it.copy(isSortDropDownMenuShow = false) }
      AlbumUiEvent.ShowSortDropDownMenu -> mUiState.update { it.copy(isSortDropDownMenuShow = true) }
      is AlbumUiEvent.UpdateSortOrder -> updateSortOrder(event.isDec)
      is AlbumUiEvent.UpdateSortType -> updateSortType(event.sortType)
    }
  }

  private fun getSortState() = viewModelScope.launch {
    albumSortDataStoreManager.sortState.collectLatest { sort ->
      mUiState.update { it.copy(sortState = sort) }
    }
  }

  private fun getAlbumData() = viewModelScope.launch {
    combine(
      musicSource.album(),
      albumSortDataStoreManager.sortState,
    ) { songs, sortState ->
      sortMusic(list = songs, isDescending = sortState.isDec, sortType = sortState.sortType)
    }.collect { albumData ->
      mUiState.update { it.copy(isLoading = false, albumList = albumData) }
    }
  }

  private fun updateSortType(songsSortType: CategorizedSortType) = viewModelScope.launch {
    albumSortDataStoreManager.updateSortType(songsSortType)
  }

  private fun updateSortOrder(boolean: Boolean) = viewModelScope.launch {
    albumSortDataStoreManager.updateSortOrder(boolean)
  }

}