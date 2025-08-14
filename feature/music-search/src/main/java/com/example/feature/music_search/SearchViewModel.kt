package com.example.feature.music_search

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.domain.respository.SearchMusicRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
  private var searchMusicManager: SearchMusicRepositoryImpl,
) : ViewModel() {

  private var mUiState = MutableStateFlow(SearchScreenUiState())
  val searchScreenUiState = mUiState
    .onStart {
      loadSearchData()
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      SearchScreenUiState(),
    )

  fun loadSearchData() = viewModelScope.launch {
    searchMusicManager.searchList.collectLatest { searchData ->
      mUiState.update {
        it.copy(
          isLoading = false,
          searchList = searchData,
        )
      }
    }
  }

  @OptIn(FlowPreview::class)
  fun onEvent(event: SearchScreenUiEvent) {
    when (event) {
      is SearchScreenUiEvent.OnSearchTextField -> {
        mUiState.update { it.copy(searchTextFieldValue = event.newValue) }
        searchMusic(input = event.newValue)
      }
      SearchScreenUiEvent.OnClearSearchTextField -> mUiState.update { it.copy(searchTextFieldValue = "") }
    }
  }

  private fun searchMusic(input: String) = viewModelScope.launch {
    searchMusicManager.search(input)
  }

}