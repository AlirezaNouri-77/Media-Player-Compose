package com.example.core.music_media3.util

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.provider.Settings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class DeviceVolumeManager(var context: Context) {
    private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    private var previews = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    private var currentVolume = 0

    // Another way for listen volume change by flow if we don't want to using contentObserver
    var volumeChangeListener: Flow<Int> = flow {
        while (currentCoroutineContext().isActive) {
            delay(100L)
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (currentVolume != previews) {
                emit(currentVolume)
                previews = currentVolume
            }
        }
    }

    fun volumeChangeListener(): Flow<Int> {
        return callbackFlow {
            val contentObserver = object : ContentObserver(null) {
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    trySend(currentVolume)
                }
            }
            context.contentResolver.registerContentObserver(
                Settings.System.CONTENT_URI,
                true,
                contentObserver,
            )
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            trySend(currentVolume)

            awaitClose {
                context.contentResolver.unregisterContentObserver(contentObserver)
            }
        }.distinctUntilChanged()
    }

    fun setVolume(input: Float) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, input.coerceIn(0f..maxVolume.toFloat()).toInt(), 0)
        previews = input.toInt()
    }

    fun getMaxVolume() = maxVolume
}
