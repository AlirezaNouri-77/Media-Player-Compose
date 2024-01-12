package com.example.mediaplayerjetpackcompose.domain.api

import android.content.ContentResolver
import android.net.Uri
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.flow.Flow

interface MediaRepositoryGetContentByUri<out T> {
  suspend fun getMediaInformationByUri(mContentResolver: ContentResolver, uri: Uri): Flow<T>
}