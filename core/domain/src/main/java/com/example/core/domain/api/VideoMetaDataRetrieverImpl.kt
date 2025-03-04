package com.example.core.domain.api

import android.net.Uri
import com.example.core.model.VideoModel

interface VideoMediaMetaDataImpl {
  suspend fun get(uri: Uri): VideoModel?
}