package com.example.mediaplayerjetpackcompose.domain.api

import android.content.ContentResolver
import android.content.Context
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepositoryImpl<out T> {
  suspend fun getMedia(mContentResolver: ContentResolver, context: Context): Flow<List<T>>
}