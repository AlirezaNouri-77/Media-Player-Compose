package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.core.data.repository.MusicSourceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

class ArtistViewModel(
  private var musicSourceRepository: MusicSourceRepository,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  var artist = musicSourceRepository.artist()
    .onCompletion { isLoading = false }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

}