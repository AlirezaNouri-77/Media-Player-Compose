package com.example.mediaplayerjetpackcompose.core.domain.api

import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicRepositoryImpl {
  fun getSongs(): Flow<List<MusicModel>>
}