package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.text.TextAnnotation.Position
import androidx.media3.session.MediaController
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.PlayBackHandler
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MusicPageViewModel(
  private var musicMediaStoreRepository: MusicMediaStoreRepository,
  private var myApplication: ApplicationClass,
  private var playBackHandler: PlayBackHandler,
) : AndroidViewModel(myApplication) {
  
  var musicList = mutableListOf<MusicMediaModel>()
  var currentMusicState = playBackHandler.musicState
  var currentMusicPosition = playBackHandler.currentMusicPosition.stateIn(
	viewModelScope,
	SharingStarted.Eagerly,
	0L
  )
  
  init {
	getMusic()
  }
  
  fun moveToNext() = playBackHandler.moveToNext()
  fun moveToPrevious() = playBackHandler.moveToPrevious()
  fun pauseMusic() = playBackHandler.pauseMusic()
  fun resumeMusic() = playBackHandler.resumeMusic()
  fun seekToPosition(position: Long) = playBackHandler.seekToPosition(position)
  fun getImageArt(artwork: ByteArray): Bitmap {
	val bitmap = BitmapFactory.decodeByteArray(
	  artwork,
	  0,
	  artwork.size
	)
	return Bitmap.createScaledBitmap(bitmap, 300, 300, false)
  }
  
  fun playMusic(index: Int, musicList: List<MusicMediaModel>) = viewModelScope.launch {
	playBackHandler.playMusic(index, musicList)
  }
  
  private fun getMusic() {
	viewModelScope.launch {
	  musicMediaStoreRepository.getMedia(
		mContentResolver = myApplication.contentResolver,
		context = myApplication.applicationContext,
	  ).collectLatest {
		musicList.addAll(it)
	  }
	}
  }
  
  companion object {
	var Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application =
		  checkNotNull((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as ApplicationClass
		MusicPageViewModel(
		  myApplication = application,
		  musicMediaStoreRepository = application.musicMediaStoreRepository,
		  playBackHandler = application.playBackHandler,
		)
	  }
	}
  }
  
  
}