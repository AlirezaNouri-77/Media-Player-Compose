package com.shermanrex.core.music_media3

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicPlayerService : MediaLibraryService(), KoinComponent {
    private var mediaSession: MediaLibrarySession? = null
    private lateinit var mediaLibrarySessionCallback: MediaLibrarySession.Callback
    val exoPlayerHelperClass: ExoPlayerHelperClass by inject()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        mediaLibraryCallBack()

        mediaSession = MediaLibrarySession.Builder(this, exoPlayerHelperClass.exoplayer, mediaLibrarySessionCallback).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private fun mediaLibraryCallBack() {
        mediaLibrarySessionCallback = object : MediaLibrarySession.Callback {}
    }
}
