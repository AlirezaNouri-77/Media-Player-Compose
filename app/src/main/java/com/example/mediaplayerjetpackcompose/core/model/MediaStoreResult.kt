package com.example.mediaplayerjetpackcompose.core.model

sealed class MediaStoreResult<out T> {
  data object Loading : MediaStoreResult<Nothing>()
  data object Empty : MediaStoreResult<Nothing>()
  data class Result<T>(var result: List<T>) : MediaStoreResult<T>()
}