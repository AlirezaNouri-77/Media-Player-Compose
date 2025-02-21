package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.category


import androidx.lifecycle.ViewModel
import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import kotlinx.coroutines.flow.combine

class CategoryViewModel(
  musicSource: MusicSource,
) : ViewModel() {

  var categoryList = combine(
    musicSource.album(),
    musicSource.folder(),
    musicSource.artist(),
  ) { album, folder, artist ->
    Category(
      album = album,
      folder = folder,
      artist = artist,
    )
  }

}
