package com.example.core.domain.respository

import com.example.core.model.MediaStoreResult
import com.example.core.model.VideoModel
import kotlinx.coroutines.flow.Flow

interface VideoRepositoryImpl {
  fun getVideos(): Flow<MediaStoreResult<VideoModel>>
}