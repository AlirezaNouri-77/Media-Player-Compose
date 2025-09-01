package com.example.core.music_media3.di

import android.annotation.SuppressLint
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import com.example.core.common.ExoPlayerType
import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.util.DeviceVolumeManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
var MusicMedia3Module = module {

    single {
        MusicServiceConnection(androidApplication().applicationContext)
    }

    single(ExoPlayerType.MUSIC.qualifier) {
        val attributes = AudioAttributes.Builder()
            .setUsage(USAGE_MEDIA)
            .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        ExoPlayer.Builder(androidApplication().applicationContext)
            .setSeekParameters(SeekParameters.EXACT)
            .setAudioAttributes(attributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }

    single { DeviceVolumeManager(androidApplication().applicationContext) }
}
