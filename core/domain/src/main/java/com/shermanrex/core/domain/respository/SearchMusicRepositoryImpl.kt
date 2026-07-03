package com.shermanrex.core.domain.respository

import com.shermanrex.core.model.MusicModel
import kotlinx.coroutines.flow.StateFlow

interface SearchMusicRepositoryImpl {
    var searchList: StateFlow<List<MusicModel>>

    suspend fun search(input: String)
}
