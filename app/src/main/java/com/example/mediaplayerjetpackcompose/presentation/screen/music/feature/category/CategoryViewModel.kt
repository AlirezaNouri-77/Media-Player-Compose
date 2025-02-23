package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.category


import androidx.lifecycle.ViewModel
import com.example.mediaplayerjetpackcompose.core.data.repository.MusicSourceRepository
import kotlinx.coroutines.flow.combine

class CategoryViewModel(
  musicSourceRepository: MusicSourceRepository,
) : ViewModel() {

  var categoryList = combine(
    musicSourceRepository.album(),
    musicSourceRepository.folder(),
    musicSourceRepository.artist(),
  ) { album, folder, artist ->
    Category(
      album = album,
      folder = folder,
      artist = artist,
    )
  }

}
