package com.example.core.data.repository

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.StateFlow

interface SearchMusicRepositoryImpl {
  var searchList: StateFlow<List<MusicModel>>
  suspend fun search(input: String)
}