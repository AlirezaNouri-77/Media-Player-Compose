package com.shermanrex.video_media3.model

import android.net.Uri
import com.shermanrex.core.model.VideoModel

interface VideoMediaMetaDataImpl {
    suspend fun get(uri: Uri): VideoModel?
}
