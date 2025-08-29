package com.example.core.domain.respository

import com.example.core.model.MusicModel
import kotlinx.coroutines.flow.StateFlow

interface SearchMusicRepositoryImpl {
    var searchList: StateFlow<List<MusicModel>>

    suspend fun search(input: String)
}
