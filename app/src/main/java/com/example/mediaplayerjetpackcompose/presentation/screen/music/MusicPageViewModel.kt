package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.util.onDefaultDispatcher
import com.example.mediaplayerjetpackcompose.data.util.onIoDispatcher
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicPageViewModel(
  private var musicMediaStoreRepository: MediaStoreRepositoryImpl<MusicModel>,
  private var musicServiceConnection: MusicServiceConnection,
  private var dataBaseDao: DataBaseDao,
  private var mediaThumbnailUtil: MediaThumbnailUtil,
) : ViewModel() {

  private var originalMusicList = mutableStateListOf<MusicModel>()

  var musicList = mutableStateListOf<MusicModel>()
  var favoriteMusicList = mutableStateListOf<MusicModel>()

  var musicCategoryList = mutableStateListOf<MusicModel>()
  var artistsMusicMap = mutableStateListOf<CategoryMusicModel>()
  var albumMusicMap = mutableStateListOf<CategoryMusicModel>()

  var musicArtworkColorPalette by mutableIntStateOf(MediaThumbnailUtil.DefaultColorPalette)

  val currentRepeatMode = musicServiceConnection.currentRepeatMode
  var favoriteListMediaId = mutableStateListOf<String>()
  var isLoading by mutableStateOf(true)

  var sortState by mutableStateOf(SortState(SortTypeModel.NAME, false))
  var currentTabState by mutableStateOf(TabBarPosition.MUSIC)

  var isFullPlayerShow by mutableStateOf(false)
  var currentMusicPosition = mutableFloatStateOf(0f)
  var currentPagerPage = mutableIntStateOf(0)

  var pagerItemList = musicServiceConnection.pagerList
  var currentMusicState = musicServiceConnection.currentMediaState

  init {
    getFavorite()
    getMusic()
    viewModelScope.launch {
      musicServiceConnection
        .musicPosition()
        .stateIn(this, SharingStarted.WhileSubscribed(), 0L)
        .collectLatest {
          currentMusicPosition.floatValue = it.toFloat()
        }
    }
  }

  fun getColorPaletteFromArtwork(uri: Uri) {
    viewModelScope.launch {
      val bitmap = mediaThumbnailUtil.getMusicArt(uri)
      musicArtworkColorPalette = MediaThumbnailUtil.getMainColorOfBitmap(bitmap)
    }
  }

  fun sortMusicListByCategory(
    list: MutableList<MusicModel>
  ): MutableList<MusicModel> {
    viewModelScope.launch {
      onDefaultDispatcher {
        when (sortState.sortType) {
          SortTypeModel.NAME -> if (sortState.isDec) list.sortByDescending { it.name } else list.sortBy { it.name }
          SortTypeModel.ARTIST -> if (sortState.isDec) list.sortByDescending { it.artist } else list.sortBy { it.artist }
          SortTypeModel.DURATION -> if (sortState.isDec) list.sortByDescending { it.duration } else list.sortBy { it.duration }
          SortTypeModel.SIZE -> if (sortState.isDec) list.sortByDescending { it.size } else list.sortBy { it.size }
        }
      }
    }.invokeOnCompletion {
      updateMediaItemListAfterSort(list)
    }
    return list
  }

  private fun updateMediaItemListAfterSort(list: List<MusicModel>) =
    viewModelScope.launch {
      val index = list.indexOfFirst { it.musicId.toString() == currentMusicState.value.mediaId }
      if (index == -1) return@launch

      currentPagerPage.intValue = index
      pagerItemList.clear()
      pagerItemList.addAll(list)
      musicServiceConnection.updateMediaList(index, musicList, currentMusicPosition.floatValue.toLong())
    }

  fun moveToNext() {
    if (!musicServiceConnection.hasNextItem()) return
    musicServiceConnection.moveToNext()
    currentMusicPosition.floatValue = 0f
    currentPagerPage.intValue += 1
  }

  fun moveToPrevious(seekToStart: Boolean = false) {
    if (!musicServiceConnection.hasPreviewItem()) return
    if (currentMusicPosition.floatValue <= 15_000 || seekToStart) {
      musicServiceConnection.moveToPreviousMediaItem()
      currentPagerPage.intValue -= 1
    } else musicServiceConnection.moveToPrevious()
    currentMusicPosition.floatValue = 0f
  }

  fun pauseMusic() = musicServiceConnection.pauseMusic()

  fun resumeMusic() = musicServiceConnection.resumeMusic()

  fun seekToPosition(position: Long) {
    musicServiceConnection.seekToPosition(position)
    currentMusicPosition.floatValue = position.toFloat()
  }

  fun setRepeatMode(repeatMode: Int) = musicServiceConnection.setPlayerRepeatMode(repeatMode)

  fun handleFavoriteSongs(mediaId: String) {
    viewModelScope.launch {
      onIoDispatcher {
        if (mediaId in favoriteListMediaId) {
          dataBaseDao.deleteFavoriteSong(mediaId)
        } else {
          dataBaseDao.insertFavoriteSong(FavoriteMusicModel(mediaId = mediaId))
        }
      }
    }
  }

  fun searchMusic(input: String) = viewModelScope.launch {
    if (input.isNotEmpty() || input.isNotEmpty()) {
      musicList.clear()
        .also {
          musicList.addAll(originalMusicList.filter { it.name.lowercase().contains(input.lowercase()) })
        }
    } else {
      musicList.clear().also { musicList.addAll(originalMusicList) }
    }
  }

  fun playMusic(index: Int, musicList: List<MusicModel>) = viewModelScope.launch {
    currentPagerPage.intValue = index
    musicServiceConnection.pagerList.clear()
    musicServiceConnection.pagerList.addAll(musicList)
    musicServiceConnection.playMusic(index, musicList)
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

          MediaStoreResult.Loading -> isLoading = true

          is MediaStoreResult.Result -> {
            musicList.addAll(result.result)
            originalMusicList.addAll(result.result)
            artistsMusicMap.addAll(result.result.groupBy { by -> by.artist }.map { CategoryMusicModel(it.key, it.value) })
            albumMusicMap.addAll(result.result.groupBy { by -> by.album }.map { CategoryMusicModel(it.key, it.value) })
            isLoading = false
          }

          else -> {}
        }
      }

  }

}