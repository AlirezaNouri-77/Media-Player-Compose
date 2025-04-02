package com.example.core.music_media3.util

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.provider.Settings
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class DeviceVolumeManager(
  var context: Context,
) : ContentObserver(null) {

  private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
  private var maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

  private var previews = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

  private var _currentMusicLevelVolume = MutableSharedFlow<Int>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
  var currentMusicLevelVolume = _currentMusicLevelVolume.asSharedFlow()

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

  override fun onChange(selfChange: Boolean) {
    super.onChange(selfChange)
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    if (currentVolume != previews) {
      _currentMusicLevelVolume.tryEmit(currentVolume)
      previews = currentVolume
    }
  }

  fun setVolume(input: Float) {
    previews = input.toInt()
    _currentMusicLevelVolume.tryEmit(input.toInt())
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, input.coerceIn(0f..maxVolume.toFloat()).toInt(), 0)
  }

  fun getMaxVolume() = maxVolume

  fun setInitialVolume(){
    _currentMusicLevelVolume.tryEmit(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
  }

  fun registerContentObserver() {
    context.contentResolver.registerContentObserver(
      Settings.System.CONTENT_URI, true, this
    )
  }

  fun unRegisterContentObserver() {
    context.contentResolver.unregisterContentObserver(this)
  }

}