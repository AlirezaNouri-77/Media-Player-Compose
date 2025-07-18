package com.example.feature.music_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.util.sortMusic
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.datastore.SortDataStoreManagerImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArtistViewModel(
  private var musicSource: MusicSourceImpl,
  private var artistSortDataStoreManager: SortDataStoreManagerImpl<CategorizedSortModel>,
) : ViewModel() {

  var isLoading by mutableStateOf(true)
    private set

  val sortState = artistSortDataStoreManager.sortState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    CategorizedSortModel(CategorizedSortType.NAME, false)
  )

  val artist = combine(
    musicSource.artist(),
    artistSortDataStoreManager.sortState,
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

  fun updateSortType(songsSortType: CategorizedSortType) = viewModelScope.launch {
    artistSortDataStoreManager.updateSortType(songsSortType)
  }

  fun updateSortIsDec(boolean: Boolean) = viewModelScope.launch {
    artistSortDataStoreManager.updateSortOrder(boolean)
  }

}