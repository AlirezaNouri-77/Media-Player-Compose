package com.shermanrex.core.domain.respository

import com.shermanrex.core.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicRepositoryImpl {
    fun getSongs(): Flow<List<MusicModel>>
}
