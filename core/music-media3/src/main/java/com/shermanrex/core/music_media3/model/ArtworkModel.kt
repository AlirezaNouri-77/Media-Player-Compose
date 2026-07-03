package com.shermanrex.core.music_media3.model

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class ArtworkModel(
    val musicId: String,
    val uri: Uri,
    val name: String,
    val artist: String,
)
