package com.shermanrex.core.music_media3

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerHelperClass(
    val exoplayer: ExoPlayer,
) {
    @OptIn(UnstableApi::class)
    fun setPauseAtEndMedia(boolean: Boolean) {
        exoplayer.pauseAtEndOfMediaItems = boolean
    }
}
