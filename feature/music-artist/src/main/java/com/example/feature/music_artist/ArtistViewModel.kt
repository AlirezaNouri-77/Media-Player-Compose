package com.example.feature.music_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.api.MusicSourceImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

class ArtistViewModel(
  private var musicSource: MusicSourceImpl,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  var artist = musicSource.artist()
    .onCompletion { isLoading = false }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

}