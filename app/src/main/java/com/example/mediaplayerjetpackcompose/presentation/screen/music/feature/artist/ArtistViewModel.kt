package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ArtistViewModel(
  private var musicSource: MusicSource,
) : ViewModel() {

  var artist = musicSource.artist()
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

}