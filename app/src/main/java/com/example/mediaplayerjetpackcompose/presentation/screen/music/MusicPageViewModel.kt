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
import com.example.mediaplayerjetpackcompose.data.util.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.application.ApplicationClass
import com.example.mediaplayerjetpackcompose.data.database.databaseClass.AppDataBase
import com.example.mediaplayerjetpackcompose.data.util.onIoDispatcher
import com.example.mediaplayerjetpackcompose.data.util.onMainDispatcher
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.FavoriteModel
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.domain.model.SortBarModel
import com.example.mediaplayerjetpackcompose.domain.model.TabBarPosition
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicPageViewModel(
  private var myApplication: ApplicationClass,
  private var musicMediaStoreRepository: MusicMediaStoreRepository,
  private var getMediaArt: GetMediaArt,
  private var musicServiceConnection: MusicServiceConnection,
  private var appDataBase: AppDataBase,
) : AndroidViewModel(myApplication) {

  private var originalMusicList = mutableStateListOf<MusicMediaModel>()
  var musicList = mutableStateListOf<MusicMediaModel>()
  var musicCategoryList = mutableStateListOf<MusicMediaModel>()
  var artistsMusicMap = mutableStateListOf<CategoryList>()
  var albumMusicMap = mutableStateListOf<CategoryList>()
  var favoriteMusicList = mutableStateListOf<MusicMediaModel>()

  val currentRepeatMode = musicServiceConnection.currentRepeatMode
  var favoriteListMediaId = mutableStateListOf<String>()
  var isLoading by mutableStateOf(true)

  var currentListSort = mutableStateOf(SortBarModel.NAME)
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
    getFavorite()
    getMusic()
  }

  fun sortMusicListByCategory(
    list: MutableList<MusicMediaModel>
  ): MutableList<MusicMediaModel> {
    viewModelScope.launch {
      onMainDispatcher {
        if (!isDec.value) {
          when (currentListSort.value) {
            SortBarModel.NAME -> list.sortBy { it.name }
            SortBarModel.ARTIST -> list.sortBy { it.artist }
            SortBarModel.DURATION -> list.sortBy { it.duration }
            SortBarModel.SIZE -> list.sortBy { it.size }
          }
        } else {
          when (currentListSort.value) {
            SortBarModel.NAME -> list.sortByDescending { it.name }
            SortBarModel.ARTIST -> list.sortByDescending { it.artist }
            SortBarModel.DURATION -> list.sortByDescending { it.duration }
            SortBarModel.SIZE -> list.sortByDescending { it.size }
          }
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

  fun getImageArt(uri: Uri): Bitmap = getMediaArt.getMusicArt(uri, 350, 350)
  fun moveToNext() = musicServiceConnection.moveToNext()
  fun moveToPrevious() = musicServiceConnection.moveToPrevious()
  fun pauseMusic() = musicServiceConnection.pauseMusic()
  fun resumeMusic() = musicServiceConnection.resumeMusic()
  fun seekToPosition(position: Long) = musicServiceConnection.seekToPosition(position)
  fun setRepeatMode(repeatMode: Int) = musicServiceConnection.setPlayerRepeatMode(repeatMode)


  fun handleFavoriteSongs(mediaId: String) {
    viewModelScope.launch {
      onIoDispatcher {
        if (mediaId in favoriteListMediaId) {
          appDataBase.dataBaseDao().deleteFavoriteSong(mediaId)
        } else {
          appDataBase.dataBaseDao().insertFavoriteSong(FavoriteModel(mediaId = mediaId))
        }
      }
    }
  }

  fun searchMusic(input: String) = viewModelScope.launch {
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

  fun playMusic(index: Int, musicList: List<MusicMediaModel>) = viewModelScope.launch {
    onMainDispatcher {
      musicServiceConnection.playMusic(index, musicList)
    }
  }

  private fun getFavorite() = viewModelScope.launch {
    appDataBase.dataBaseDao().getAllFaFavoriteSongs()
      .collectLatest { favoriteList ->
        favoriteListMediaId.clear()
        favoriteListMediaId.addAll(favoriteList.map { it.mediaId }.toSet())
      }
  }

  private fun getMusic() = viewModelScope.launch {
    musicMediaStoreRepository.getMedia()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MediaStoreResult.Initial)
      .collect { result ->
        when (result) {
          MediaStoreResult.Loading -> {
            isLoading = true
          }

          is MediaStoreResult.Result -> {
            musicList.addAll(result.result)
            originalMusicList.addAll(result.result)
            artistsMusicMap.addAll(result.result.groupBy { by -> by.artist }.map { CategoryList(it.key, it.value) })
            albumMusicMap.addAll(result.result.groupBy { by -> by.album }.map { CategoryList(it.key, it.value) })
            isLoading = false
          }

          else -> {}
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
          getMediaArt = application.getMediaArt,
          musicServiceConnection = application.musicServiceConnection,
          appDataBase = application.appDataBase,
        )
      }
    }
  }
}

data class CategoryList(
  var name: String,
  var list: List<MusicMediaModel>
)