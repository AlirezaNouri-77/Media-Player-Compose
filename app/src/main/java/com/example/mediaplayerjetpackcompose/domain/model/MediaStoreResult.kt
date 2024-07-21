package com.example.mediaplayerjetpackcompose.domain.model

sealed class MediaStoreResult<T> {
  data object Initial : MediaStoreResult<Nothing>()
  data object Loading : MediaStoreResult<Nothing>()
  data class Result<T>(var result: List<T>) : MediaStoreResult<T>()
}