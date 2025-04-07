package com.example.feature.music_categorydetail

import androidx.compose.runtime.Immutable
import com.example.core.data.repository.albumName
import com.example.core.data.repository.artistName
import com.example.core.data.repository.folderName
import com.example.core.model.MusicModel

@Immutable
data class CategorizedMusicData(
  val album: Map<albumName, List<MusicModel>>,
  val artist: Map<artistName, List<MusicModel>>,
  val folder: Map<folderName, List<MusicModel>>,
)
