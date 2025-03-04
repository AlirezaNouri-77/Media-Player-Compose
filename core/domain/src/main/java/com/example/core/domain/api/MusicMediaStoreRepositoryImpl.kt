package com.example.core.domain.api

import com.example.core.model.MediaStoreResult
import com.example.core.model.VideoModel
import kotlinx.coroutines.flow.Flow

interface VideoSourceImpl {
  fun getVideos(): Flow<MediaStoreResult<VideoModel>>
}