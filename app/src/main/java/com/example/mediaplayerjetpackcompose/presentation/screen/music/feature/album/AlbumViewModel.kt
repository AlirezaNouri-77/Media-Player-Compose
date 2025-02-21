package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AlbumViewModel(
  private var musicSource: MusicSource,
) : ViewModel() {

  var album = musicSource.album()
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

}