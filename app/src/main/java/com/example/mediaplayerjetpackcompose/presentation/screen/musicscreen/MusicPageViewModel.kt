package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.PlayBackHandler
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.onMainDispatcher
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPageViewModel(
  private var musicMediaStoreRepository: MusicMediaStoreRepository,
  private var myApplication: ApplicationClass,
  private var getMediaArt: GetMediaArt,
  private var playBackHandler: PlayBackHandler,
) : AndroidViewModel(myApplication) {

  var MainMusicList = mutableStateListOf<MusicMediaModel>()
  var musicList = mutableStateListOf<MusicMediaModel>()
  var musicCategoryList = mutableStateListOf<MusicMediaModel>()
  var artistsMusicMap = mapOf<String, List<MusicMediaModel>>()
  var albumMusicMap = mapOf<String, List<MusicMediaModel>>()

  val currentRepeatMode = playBackHandler.currentRepeatMode
  var isLoading = mutableStateOf(true)

  var currentListSort = mutableStateOf(SortItem.NAME)
  var currentTabState by mutableIntStateOf(0)
  var isDec = mutableStateOf(false)
  var showBottomSheet = mutableStateOf(false)

  var currentMusicState = playBackHandler.mediaCurrentState
  var currentMusicPosition = playBackHandler.currentMusicPosition.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0L
  )

  init {
    getMusic()
  }

  fun sortMusicListByCategory(
    list: MutableList<MusicMediaModel>
  ): MutableList<MusicMediaModel> {
    viewModelScope.launch(Dispatchers.Main) {
      if (!isDec.value) {
        when (currentListSort.value) {
          SortItem.NAME -> list.sortBy { it.name }
          SortItem.ARTIST -> list.sortBy { it.artist }
          SortItem.DURATION -> list.sortBy { it.duration }
          SortItem.SIZE -> list.sortBy { it.size }
        }
      } else {
        when (currentListSort.value) {
          SortItem.NAME -> list.sortByDescending { it.name }
          SortItem.ARTIST -> list.sortByDescending { it.artist }
          SortItem.DURATION -> list.sortByDescending { it.duration }
          SortItem.SIZE -> list.sortByDescending { it.size }
        }
      }
    }.invokeOnCompletion {
      updateMediaItemList(list)
    }
    return list
  }

  private fun updateMediaItemList(list: List<MusicMediaModel>) =
    viewModelScope.launch {
      onMainDispatcher {
        val index = list.indexOfFirst { it.musicId.toString() == currentMusicState.value.mediaId }
        //playBackHandler.updateMediaList(index, musicList, currentMusicPosition.value)
      }
    }

  fun moveToNext() = playBackHandler.moveToNext()
  fun moveToPrevious() = playBackHandler.moveToPrevious()
  fun pauseMusic() = playBackHandler.pauseMusic()
  fun resumeMusic() = playBackHandler.resumeMusic()
  fun seekToPosition(position: Long) = playBackHandler.seekToPosition(position)

  fun setRepeatMode(repeatMode: Int) {
    playBackHandler.setPlayerRepeatMode(repeatMode)
  }

  fun searchMusic(input: String) {
    viewModelScope.launch {
      onMainDispatcher {
        if (input.isNotEmpty() || input.isNotEmpty()) {
          musicList.clear()
            .also {
              musicList.addAll(MainMusicList.filter { it.name.lowercase().contains(input.lowercase()) })
            }
        } else {
          musicList.clear().also { musicList.addAll(MainMusicList) }
        }
      }
    }
  }

  fun playMusic(index: Int, musicList: List<MusicMediaModel>) =
    viewModelScope.launch {
      onMainDispatcher {
        playBackHandler.playMusic(index, musicList)
      }
    }

  private fun getMusic() {
    viewModelScope.launch {
      musicMediaStoreRepository.getMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MediaStoreResult.Initial)
        .collect {
          when (it) {
            is MediaStoreResult.Result -> {
              onMainDispatcher {
                musicList.addAll(it.result)
                MainMusicList.addAll(it.result)
                artistsMusicMap = it.result.groupBy { by -> by.artist }
                albumMusicMap = it.result.groupBy { by -> by.album }
              }
            }
            else -> {}
          }
        }
    }
    isLoading.value = false
  }

  fun getImageArt(uri: Uri): Bitmap {
    return getMediaArt.getMusicArt(uri, 350, 350)
  }

  companion object {
    var Factory: ViewModelProvider.Factory = viewModelFactory {
      initializer {
        val application =
          checkNotNull((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as ApplicationClass
        MusicPageViewModel(
          myApplication = application,
          musicMediaStoreRepository = application.musicMediaStoreRepository,
          getMediaArt = application.getMediaArt,
          playBackHandler = application.playBackHandler,
        )
      }
    }
  }

}

enum class SortItem(var sortName: String) {
  NAME("Name"),
  ARTIST("Artist"),
  DURATION("Duration"),
  SIZE("Size"),
}