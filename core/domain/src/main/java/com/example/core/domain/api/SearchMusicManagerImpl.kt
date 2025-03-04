package com.example.core.domain.api

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.StateFlow

interface SearchMusicManagerImpl {
  var searchList: StateFlow<List<MusicModel>>
  suspend fun search(input: String)
}