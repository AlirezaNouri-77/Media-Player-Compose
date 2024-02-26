package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediaplayerjetpackcompose.data.application.ApplicationClass
import com.example.mediaplayerjetpackcompose.data.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.onMainDispatcher
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.domain.model.TabBarPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicPageViewModel(
  private var musicMediaStoreRepository: MusicMediaStoreRepository,
  private var myApplication: ApplicationClass,
  private var getMediaArt: GetMediaArt,
  private var musicServiceConnection: MusicServiceConnection,
) : AndroidViewModel(myApplication) {

  private var originalMusicList = mutableStateListOf<MusicMediaModel>()
  var musicList = mutableStateListOf<MusicMediaModel>()
  var musicCategoryList = mutableStateListOf<MusicMediaModel>()
  var artistsMusicMap = mapOf<String, List<MusicMediaModel>>()
  var albumMusicMap = mapOf<String, List<MusicMediaModel>>()

  val currentRepeatMode = musicServiceConnection.currentRepeatMode
  var isLoading by mutableStateOf(true)

  var currentListSort = mutableStateOf(SortItem.NAME)
  var currentTabState by mutableStateOf(TabBarPosition.MUSIC)
  var isDec = mutableStateOf(false)
  var showBottomSheet = mutableStateOf(false)

  var currentMusicState = musicServiceConnection.mediaCurrentState
  var currentMusicPosition = musicServiceConnection.currentMusicPosition.stateIn(
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
        musicServiceConnection.updateMediaList(index, musicList, currentMusicPosition.value)
      }
    }

  fun moveToNext() = musicServiceConnection.moveToNext()
  fun moveToPrevious() = musicServiceConnection.moveToPrevious()
  fun pauseMusic() = musicServiceConnection.pauseMusic()
  fun resumeMusic() = musicServiceConnection.resumeMusic()
  fun seekToPosition(position: Long) = musicServiceConnection.seekToPosition(position)

  fun setRepeatMode(repeatMode: Int) {
    musicServiceConnection.setPlayerRepeatMode(repeatMode)
  }


  fun searchMusic(input: String) {
    viewModelScope.launch {
      onMainDispatcher {
        if (input.isNotEmpty() || input.isNotEmpty()) {
          musicList.clear()
            .also {
              musicList.addAll(originalMusicList.filter { it.name.lowercase().contains(input.lowercase()) })
            }
        } else {
          musicList.clear().also { musicList.addAll(originalMusicList) }
        }
      }
    }
  }

  fun playMusic(index: Int, musicList: List<MusicMediaModel>) =
    viewModelScope.launch {
      onMainDispatcher {
        musicServiceConnection.playMusic(index, musicList)
      }
    }

  private fun getMusic() {
    viewModelScope.launch {
      musicMediaStoreRepository.getMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MediaStoreResult.Initial)
        .collect {
          when (it) {
            MediaStoreResult.Loading -> {
              isLoading = true
            }

            is MediaStoreResult.Result -> {
              musicList.addAll(it.result)
              originalMusicList.addAll(it.result)
              artistsMusicMap = it.result.groupBy { by -> by.artist }
              albumMusicMap = it.result.groupBy { by -> by.album }
              isLoading = false
            }

            else -> {}
          }
        }
    }
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
          musicServiceConnection = application.musicServiceConnection,
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