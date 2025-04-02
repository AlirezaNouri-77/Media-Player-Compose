package com.example.core.data.repository

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicRepositoryImpl {
  fun getSongs(): Flow<List<MusicModel>>
}