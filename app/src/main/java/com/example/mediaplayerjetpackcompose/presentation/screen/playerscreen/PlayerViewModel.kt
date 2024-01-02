package com.example.mediaplayerjetpackcompose.presentation.screen.playerscreen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.mediaplayerjetpackcompose.ApplicationClass

class PlayerViewModel(
  private var mApplicationContext: Context
) : ViewModel() {
  
  lateinit var exoPlayer: ExoPlayer
  var onBackPress = mutableStateOf(false)
  
  init {
	initialPlayer()
  }
  
  @SuppressLint("UnsafeOptInUsageError")
  var deviceOrientation = mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)
  
  private fun initialPlayer() {
	exoPlayer = ExoPlayer.Builder(mApplicationContext.applicationContext).build()
  }
  
  fun startPlayVideo(videoUri: Uri) {
	exoPlayer.apply {
	  MediaItem.Builder()
	  this.addMediaItem(MediaItem.fromUri(videoUri))
	  this.prepare()
	  this.play()
	}
  }
  
  fun pausePlayer() {
	exoPlayer.pause()
  }
  
  fun resumePlayer() {
	exoPlayer.play()
  }
  
  fun releasePlayer() {
	exoPlayer.pause()
	exoPlayer.clearMediaItems()
	exoPlayer.pause()
	exoPlayer.release()
	onBackPress.value = false
  }
  
  companion object {
	val Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
		PlayerViewModel(application.applicationContext)
	  }
	}
  }
}