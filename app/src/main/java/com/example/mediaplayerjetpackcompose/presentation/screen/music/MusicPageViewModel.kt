package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.util.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.util.onIoDispatcher
import com.example.mediaplayerjetpackcompose.data.util.onMainDispatcher
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.CategoryListModel
import com.example.mediaplayerjetpackcompose.domain.model.FavoriteModel
import com.example.mediaplayerjetpackcompose.domain.model.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.SortBarModel
import com.example.mediaplayerjetpackcompose.domain.model.TabBarPosition
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicPageViewModel(
  private var musicMediaStoreRepository: MediaStoreRepositoryImpl<MusicModel>,
  private var musicServiceConnection: MusicServiceConnection,
  private var getMediaArt: GetMediaArt,
  private var dataBaseDao: DataBaseDao,
) : ViewModel() {

  private var originalMusicList = mutableStateListOf<MusicModel>()
  var musicList = mutableStateListOf<MusicModel>()
  var musicCategoryList = mutableStateListOf<MusicModel>()
  var artistsMusicMap = mutableStateListOf<CategoryListModel>()
  var albumMusicMap = mutableStateListOf<CategoryListModel>()
  var favoriteMusicList = mutableStateListOf<MusicModel>()

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
    list: MutableList<MusicModel>
  ): MutableList<MusicModel> {
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

  private fun updateMediaItemList(list: List<MusicModel>) =
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
          dataBaseDao.deleteFavoriteSong(mediaId)
        } else {
          dataBaseDao.insertFavoriteSong(FavoriteModel(mediaId = mediaId))
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

  fun playMusic(index: Int, musicList: List<MusicModel>) = viewModelScope.launch {
    onMainDispatcher {
      musicServiceConnection.playMusic(index, musicList)
    }
  }

  private fun getFavorite() = viewModelScope.launch {
    dataBaseDao.getAllFaFavoriteSongs()
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
            artistsMusicMap.addAll(result.result.groupBy { by -> by.artist }.map { CategoryListModel(it.key, it.value) })
            albumMusicMap.addAll(result.result.groupBy { by -> by.album }.map { CategoryListModel(it.key, it.value) })
            isLoading = false
          }

          else -> {}
        }
      }

  }
}