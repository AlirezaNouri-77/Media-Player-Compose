package com.example.video_media3.model

import android.net.Uri
import com.example.core.model.VideoModel

interface VideoMediaMetaDataImpl {
    suspend fun get(uri: Uri): VideoModel?
}
