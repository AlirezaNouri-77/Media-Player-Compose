package com.example.mediaplayerjetpackcompose.domain.api

import com.example.mediaplayerjetpackcompose.domain.model.FavoriteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepositoryImpl<out T> {
  suspend fun getMedia(): Flow<MediaStoreResult<out T>>
}

sealed class MediaStoreResult<T> {
  data object Initial : MediaStoreResult<Nothing>()
  data object Loading : MediaStoreResult<Nothing>()
  data class Result<T>(var result: List<T>) : MediaStoreResult<T>()
}