package com.example.feature.music_categorydetail

import androidx.lifecycle.ViewModel
import com.example.core.domain.api.MusicSourceImpl
import kotlinx.coroutines.flow.combine

class CategoryViewModel(
  private val musicSource: MusicSourceImpl,
) : ViewModel() {

  var categorizedMusicDataList = combine(
    musicSource.album(),
    musicSource.folder(),
    musicSource.artist(),
  ) { album, folder, artist ->
    CategorizedMusicData(
      album = album,
      folder = folder,
      artist = artist,
    )
  }

}
