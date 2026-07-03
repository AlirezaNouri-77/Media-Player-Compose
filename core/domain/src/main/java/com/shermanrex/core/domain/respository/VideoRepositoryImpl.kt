package com.shermanrex.core.domain.respository

import com.shermanrex.core.model.MediaStoreResult
import com.shermanrex.core.model.VideoModel
import kotlinx.coroutines.flow.Flow

interface VideoRepositoryImpl {
    fun getVideos(): Flow<MediaStoreResult<VideoModel>>
}
