package com.example.mediaplayerjetpackcompose.domain.api

import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepositoryImpl<out T> {
  suspend fun getMedia(): Flow<MediaStoreResult<out T>>
}