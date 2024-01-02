package com.example.mediaplayerjetpackcompose.presentation.screen.mediascreen

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediastore.data.MediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MediaPageViewModel(
  private var contentResolver: ContentResolver,
  private var mediaStoreRepository: MediaStoreRepository,
) : ViewModel() {
  
  var mediaStoreDataList = listOf<VideoMediaModel>()
	private set
  
  init {
	getMedia()
  }
  
  private fun getMedia() {
	viewModelScope.launch {
	  mediaStoreRepository.getMedia(mContentResolver = contentResolver).stateIn(
		viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue = emptyList()
	  ).collectLatest{
		mediaStoreDataList = it
	  }
	}
  }
  
  companion object {
	val Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
		val savedStateHandle = createSavedStateHandle()
		MediaPageViewModel(
		  contentResolver = application.contentResolver,
		  mediaStoreRepository = application.mediaStoreRepository,
		)
	  }
	}
	
  }
  
}

