package com.example.core.domain.respository

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

typealias albumName = String
typealias artistName = String
typealias folderName = String

interface MusicSourceImpl {
  fun songs(): Flow<List<MusicModel>>
  fun artist(): Flow<List<Pair<artistName, List<MusicModel>>>>
  fun album(): Flow<List<Pair<albumName, List<MusicModel>>>>
  fun folder(): Flow<List<Pair<folderName, List<MusicModel>>>>
  fun favorite(): Flow<List<MusicModel>>
}