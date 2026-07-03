package com.shermanrex.feature.music_categorydetail

import androidx.compose.runtime.Immutable
import com.shermanrex.core.domain.respository.albumName
import com.shermanrex.core.domain.respository.artistName
import com.shermanrex.core.model.MusicModel

@Immutable
data class CategorizedMusicData(
    val album: List<Pair<albumName, List<MusicModel>>>,
    val artist: List<Pair<artistName, List<MusicModel>>>,
    val folder: List<Pair<artistName, List<MusicModel>>>,
)
