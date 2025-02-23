package com.example.mediaplayerjetpackcompose.core.domain.api

import com.example.mediaplayerjetpackcompose.core.model.MediaStoreResult
import com.example.mediaplayerjetpackcompose.core.model.VideoModel
import kotlinx.coroutines.flow.Flow

interface VideoRepositoryImpl {
  fun getVideos(): Flow<MediaStoreResult<VideoModel>>
}