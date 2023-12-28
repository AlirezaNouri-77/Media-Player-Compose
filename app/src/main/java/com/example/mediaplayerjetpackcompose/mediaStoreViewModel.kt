package com.example.mediaplayerjetpackcompose

import android.content.ContentResolver
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediastore.data.MediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MediaStoreViewModel(
  private var contentResolver: ContentResolver,
  private var mediaStoreRepository: MediaStoreRepository,
) : ViewModel() {
  
  var mediaStoreDataList = mutableStateListOf<VideoMediaModel>()
	private set
  
  init {
	getMedia()
  }
  
  fun getMedia() {
	viewModelScope.launch {
	  mediaStoreRepository.getMedia(mContentResolver = contentResolver).collectLatest {
		mediaStoreDataList.add(it)
	  }
	}
  }
  
  companion object {
	val Factory: ViewModelProvider.Factory = viewModelFactory {
	  initializer {
		val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
		val savedStateHandle = createSavedStateHandle()
		MediaStoreViewModel(
		  contentResolver = application.contentResolver,
		  mediaStoreRepository = application.mediaStoreRepository,
		)
	  }
	}
	
  }
  
}

