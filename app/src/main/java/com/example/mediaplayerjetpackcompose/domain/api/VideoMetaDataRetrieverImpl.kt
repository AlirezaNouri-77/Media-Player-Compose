package com.example.mediaplayerjetpackcompose.domain.api

import android.net.Uri

interface MetaDataRetrieverImpl<out T> {
  suspend fun get(uri: Uri): T
}