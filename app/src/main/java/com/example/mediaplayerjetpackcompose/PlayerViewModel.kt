package com.example.mediaplayerjetpackcompose

import android.annotation.SuppressLint
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

class PlayerViewModel(
  player: ExoPlayer
) : ViewModel() {
  
  var exoPlayer = player
  var onBackPress = mutableStateOf(false)
  @SuppressLint("UnsafeOptInUsageError")
  var deviceOrientation = mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)
  
  fun startPlayVideo(videoUri: Uri) {
	exoPlayer.apply {
	  MediaItem.Builder()
	  this.addMediaItem(MediaItem.fromUri(videoUri))
	  this.prepare()
	  this.play()
	}
  }
  
  fun playPlayer(){
	exoPlayer.play()
  }
  
  fun pausePlayer(){
	exoPlayer.pause()
  }
  
  fun stopPlayVideo() {
	exoPlayer.pause()
	exoPlayer.clearMediaItems()
	onBackPress.value = false
  }
  
  companion object {
	val Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
		PlayerViewModel(application.exoPlayer)
	  }
	}
  }
}