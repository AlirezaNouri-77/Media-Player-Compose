package com.example.feature.music_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.data.repository.SearchMusicRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
  private var favoriteMusicManager: FavoriteMusicSourceImpl,
  private var searchMusicManager: SearchMusicRepositoryImpl,
) : ViewModel() {
  
  var searchList = searchMusicManager.searchList
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000),
      emptyList(),
    )

  var favoriteMusicMediaIds = favoriteMusicManager.favoriteMusicMediaIdList.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
    emptyList(),
  )

  fun searchMusic(input: String) = viewModelScope.launch(Dispatchers.Default) {
    searchMusicManager.search(input)
  }

}