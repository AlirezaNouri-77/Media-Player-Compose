package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicPageViewModel(
  private var contentResolver: ContentResolver,
  private var musicMediaStoreRepository: MusicMediaStoreRepository,
  private var myApplication: ApplicationClass,
) : ViewModel() {
  
  var musicMediaStoreDataList = mutableStateListOf<MusicMediaModel>()
	private set
  var musicPlayer = ExoPlayer.Builder(myApplication).build()
  var currentMusic = mutableStateOf(MusicMediaModel())
  var currentListPosition by mutableIntStateOf(0)
  var currentMusicPosition by mutableFloatStateOf(0f)
  var isPlayerRunning by mutableStateOf(false)
  var shouldHideTopBarAndBottomBar by mutableStateOf(false)
  var imageBitmap = mutableIntStateOf(0)
  
  init {
	getMusic()
	musicPlayerEvent()
  }
  
  private fun musicPlayerEvent() {
	musicPlayer.addListener(object : Player.Listener {
	  override fun onIsPlayingChanged(isPlaying: Boolean) {
		isPlayerRunning = isPlaying
	  }
	  
//	  override fun onPlaybackStateChanged(playbackState: Int) {
//		if (playbackState == Player.STATE_ENDED) {
//		  moveToNextMusic()
//		}
//	  }
	})
  }
  
  fun bitmapTest(bitmap: Bitmap) = viewModelScope.launch {
	imageBitmap.intValue = Palette.from(bitmap).generate().darkVibrantSwatch!!.rgb
  }
  
  fun moveToNextMusic() {
	if (currentListPosition.plus(1) > musicMediaStoreDataList.lastIndex) return
	currentListPosition++
	currentMusic.value = musicMediaStoreDataList[currentListPosition]
	playMusic(musicMediaStoreDataList[currentListPosition].uri)
  }
  
  fun moveToBeforeMusic() {
	if (currentListPosition.minus(1) < 0) return
	currentListPosition--
	currentMusic.value = musicMediaStoreDataList[currentListPosition]
	playMusic(musicMediaStoreDataList[currentListPosition].uri)
  }
  
  fun playMusic(uri: Uri) {
	musicPlayer.setMediaItem(MediaItem.fromUri(uri))
	musicPlayer.playWhenReady
	musicPlayer.prepare()
	musicPlayer.play()
  }
  
  private fun getMusic() {
	viewModelScope.launch {
	  musicMediaStoreRepository.getMedia(mContentResolver = contentResolver).stateIn(
		viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue = emptyList()
	  ).collect {
		musicMediaStoreDataList.addAll(it)
	  }
	}
  }
  
  
  companion object {
	var Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application =
		  checkNotNull((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as ApplicationClass
		val savedStateHandle = createSavedStateHandle()
		MusicPageViewModel(
		  contentResolver = application.contentResolver,
		  myApplication = application,
		  musicMediaStoreRepository = application.musicMediaStoreRepository,
		)
	  }
	}
  }
}