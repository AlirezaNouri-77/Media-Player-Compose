package com.example.mediaplayerjetpackcompose.domain.api

import android.content.ContentResolver
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepositoryImpl<out T> {
  suspend fun getMedia(mContentResolver: ContentResolver): Flow<List<T>>
}