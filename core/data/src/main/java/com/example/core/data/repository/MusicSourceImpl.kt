package com.example.core.data.repository

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

typealias albumName = String
typealias artistName = String
typealias folderName = String

interface MusicSourceImpl {
  var songs: Flow<List<MusicModel>>
  fun artist(): Flow<Map<artistName, List<MusicModel>>>
  fun album(): Flow<Map<albumName, List<MusicModel>>>
  fun folder(): Flow<Map<folderName, List<MusicModel>>>
}