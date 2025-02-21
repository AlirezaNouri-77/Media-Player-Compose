package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.musicManager.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.data.musicManager.SearchMusicManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
  private var favoriteMusicManager: FavoriteMusicManager,
  private var searchMusicManager: SearchMusicManager,
) : ViewModel() {

  var searchList = searchMusicManager.searchResult.stateIn(
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