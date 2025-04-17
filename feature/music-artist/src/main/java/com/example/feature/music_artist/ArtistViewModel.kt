package com.example.feature.music_artist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.MusicSourceImpl
import com.example.core.data.repository.sortMapSort2
import com.example.core.model.FolderSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.MusicModel
import com.example.datastore.ArtistSortDataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArtistViewModel(
  private var musicSource: MusicSourceImpl,
  private var artistSortDataStoreManager: ArtistSortDataStoreManager,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  val sortState = artistSortDataStoreManager.artistSortState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    FolderSortModel(FolderSortType.NAME, false)
  )

  val artist = combine(
    musicSource.artist(),
    artistSortDataStoreManager.artistSortState,
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
    artistSortDataStoreManager.updateFolderSortType(songsSortType)
  }

  fun updateSortIsDec(boolean: Boolean) = viewModelScope.launch {
    artistSortDataStoreManager.updateArtistIsDescending(boolean)
  }

}