package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.content.ContentResolver
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VideoPageViewModel(
  private var contentResolver: ContentResolver,
  private var videoMediaStoreRepository: VideoMediaStoreRepository,
) : ViewModel() {
  
  var mediaStoreDataList = mutableStateListOf<VideoMediaModel>()
	private set
  
  init {
	getVideo()
  }
  
  private fun getVideo() {
	viewModelScope.launch {
	  videoMediaStoreRepository.getMedia(mContentResolver = contentResolver).stateIn(
		viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue = emptyList()
	  ).collect{
		mediaStoreDataList.addAll(it)
	  }
	}
  }
  
  companion object {
	val Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
		val savedStateHandle = createSavedStateHandle()
		VideoPageViewModel(
		  contentResolver = application.contentResolver,
		  videoMediaStoreRepository = application.videoMediaStoreRepository,
		)
	  }
	}
	
  }
  
}

