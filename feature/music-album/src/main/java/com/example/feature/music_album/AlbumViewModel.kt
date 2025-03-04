package com.example.feature.music_album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.api.MusicSourceImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

class AlbumViewModel(
  private var musicSource: MusicSourceImpl,
) : ViewModel() {

  var isLoading by mutableStateOf(true)

  var album = musicSource.album()
    .onCompletion { isLoading = false }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

}