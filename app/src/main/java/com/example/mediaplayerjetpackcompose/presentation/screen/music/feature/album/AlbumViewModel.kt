package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.album

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.core.data.repository.MusicSourceRepository
import kotlinx.coroutines.flow.SharingStarted
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

class AlbumViewModel(
  private var musicSourceRepository: MusicSourceRepository,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  var album = musicSourceRepository.album()
    .onCompletion { isLoading = false }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

}