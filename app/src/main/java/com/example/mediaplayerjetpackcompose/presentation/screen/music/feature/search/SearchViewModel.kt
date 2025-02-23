package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.core.data.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.core.data.SearchMusicManager
import com.example.mediaplayerjetpackcompose.core.domain.api.FavoriteMusicManagerImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.SearchMusicManagerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
  private var favoriteMusicManager: FavoriteMusicManagerImpl,
  private var searchMusicManager: SearchMusicManagerImpl,
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