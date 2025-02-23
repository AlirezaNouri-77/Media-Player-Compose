package com.example.mediaplayerjetpackcompose.core.domain.api

import com.example.mediaplayerjetpackcompose.core.model.CategoryMusic
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicSourceImpl {
  var songs: Flow<List<MusicModel>>
  fun favoriteSongs(): Flow<List<MusicModel>>
  fun artist(): Flow<List<CategoryMusic>>
  fun album(): Flow<List<CategoryMusic>>
  fun folder(): Flow<List<CategoryMusic>>
}