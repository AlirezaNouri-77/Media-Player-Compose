package com.example.mediaplayerjetpackcompose.domain.api

import android.content.ContentResolver
import android.net.Uri
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.flow.Flow

interface MediaStoreRepositoryImpl {
  suspend fun getMedia(mContentResolver: ContentResolver): Flow<List<VideoMediaModel>>
  suspend fun getMediaInformationByUri(mContentResolver: ContentResolver,uri: Uri): Flow<VideoMediaModel>
}