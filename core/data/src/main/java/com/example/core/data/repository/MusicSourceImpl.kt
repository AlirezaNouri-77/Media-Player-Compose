package com.example.core.data.repository

import com.example.core.model.CategoryMusic
import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicSourceImpl {
  var songs: Flow<List<MusicModel>>
  fun artist(): Flow<List<CategoryMusic>>
  fun album(): Flow<List<CategoryMusic>>
  fun folder(): Flow<List<CategoryMusic>>
}