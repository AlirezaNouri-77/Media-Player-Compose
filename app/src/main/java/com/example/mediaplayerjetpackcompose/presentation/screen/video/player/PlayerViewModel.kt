package com.example.mediaplayerjetpackcompose.presentation.screen.video.player

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
  private var mApplicationContext: Context,
  private var mediaStoreRepository: VideoMediaStoreRepository,
) : ViewModel() {
  
  lateinit var exoPlayer: ExoPlayer
  var onBackPress = mutableStateOf(false)
  var mediaInformation = mutableStateOf(VideoMediaModel())
  
  init {
	initialPlayer()
  }
  
  @SuppressLint("UnsafeOptInUsageError")
  var deviceOrientation = mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)
  
  fun getMediaInformationByUri(uri: Uri) = viewModelScope.launch {
	mediaStoreRepository.getMediaInformationByUri(mApplicationContext.contentResolver, uri).stateIn(
	  viewModelScope,
	  SharingStarted.WhileSubscribed(5000L), initialValue = VideoMediaModel()
	).collectLatest {
	  mediaInformation.value = it
	}
  }
  
  private fun initialPlayer() {
	exoPlayer = ExoPlayer.Builder(mApplicationContext.applicationContext).build()
  }
  
  fun startPlayVideo(videoUri: Uri) {
	exoPlayer.apply {
	  this.addMediaItem(MediaItem.fromUri(videoUri))
	  this.playWhenReady = true
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
		PlayerViewModel(
		  mApplicationContext = application.applicationContext,
		  mediaStoreRepository = application.videoMediaStoreRepository,
		)
	  }
	}
  }
  
}