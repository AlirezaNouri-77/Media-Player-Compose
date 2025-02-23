package com.example.mediaplayerjetpackcompose.core.domain.api

import android.net.Uri
import com.example.mediaplayerjetpackcompose.core.model.VideoModel

interface VideoMediaMetaDataImpl {
  suspend fun get(uri: Uri): VideoModel?
}