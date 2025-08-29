package com.example.feature.music_categorydetail

import androidx.compose.runtime.Immutable
import com.example.core.domain.respository.albumName
import com.example.core.domain.respository.artistName
import com.example.core.model.MusicModel

@Immutable
data class CategorizedMusicData(
    val album: List<Pair<albumName, List<MusicModel>>>,
    val artist: List<Pair<artistName, List<MusicModel>>>,
    val folder: List<Pair<artistName, List<MusicModel>>>,
)
