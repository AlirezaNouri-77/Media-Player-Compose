package com.example.core.domain.api

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicRepositoryImpl {
  fun getSongs(): Flow<List<MusicModel>>
}