package com.example.feature.music_album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.data.repository.albumName
import com.example.core.data.repository.sortMapSort2
import com.example.core.model.FolderSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.MusicModel
import com.example.datastore.AlbumSortDataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
  private var musicSource: MusicSourceImpl,
  private var albumSortDataStoreManager: AlbumSortDataStoreManager,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  val sortState = albumSortDataStoreManager.albumSortState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    FolderSortModel(FolderSortType.NAME, false),
  )

  val album: StateFlow<List<Pair<albumName, List<MusicModel>>>> = combine(
    musicSource.album(),
    albumSortDataStoreManager.albumSortState,
  ) { songs, sortState ->
    val sortedData = sortMapSort2(
      data = songs,
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