package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.content.ContentResolver
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.MetadataRetriever
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.Constant.FRAME_VIDEO
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VideoPageViewModel(
  private var applicationClass: ApplicationClass,
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
      ).collect {
        mediaStoreDataList.addAll(it)
      }
    }
  }

  fun getThumbnail(uri: Uri): ImageBitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(applicationClass.applicationContext, uri)
    return runCatching {
      mediaMetadataRetriever.getScaledFrameAtTime(FRAME_VIDEO, OPTION_CLOSEST, 150, 150)
        ?.asImageBitmap()
    }.getOrNull()
  }

  companion object {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
      initializer {
        val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
        val savedStateHandle = createSavedStateHandle()
        VideoPageViewModel(
          applicationClass = application,
          contentResolver = application.contentResolver,
          videoMediaStoreRepository = application.videoMediaStoreRepository,
        )
      }
    }

  }

}

