package com.example.core.music_media3

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.example.core.common.ExoPlayerType
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class MusicPlayerService : MediaLibraryService(), KoinComponent {
    private var mediaSession: MediaLibrarySession? = null
    private lateinit var mediaLibrarySessionCallback: MediaLibrarySession.Callback
    val musicPlayer: ExoPlayer by inject(ExoPlayerType.MUSIC.qualifier)

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        mediaLibraryCallBack()

        mediaSession = MediaLibrarySession.Builder(this, musicPlayer, mediaLibrarySessionCallback).build()
    }

    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
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
