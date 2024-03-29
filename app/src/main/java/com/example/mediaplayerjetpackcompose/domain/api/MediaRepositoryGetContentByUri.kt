package com.example.mediaplayerjetpackcompose.domain.api

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface widthMediaRepositoryGetContentByUri<out T> {
  suspend fun getMediaInformationByUri(mContentResolver: ContentResolver, uri: Uri): Flow<T>
}