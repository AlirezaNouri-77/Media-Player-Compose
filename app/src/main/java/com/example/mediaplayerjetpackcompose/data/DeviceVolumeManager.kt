package com.example.mediaplayerjetpackcompose.data

import android.content.Context
import android.media.AudioManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class DeviceVolumeManager(
  private var context: Context,
) {

  private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
  private var maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

  var volumeChangeListener: Flow<Int> = flow {
    while (currentCoroutineContext().isActive) {
      delay(100L)
      val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
      emit(currentVolume)
    }
  }

  fun setVolume(input: Float) {
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, input.toInt(), 0)
  }

  fun getMaxVolume() = maxVolume

}