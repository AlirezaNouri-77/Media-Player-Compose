package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.util.DeviceVolumeManager
import com.example.mediaplayerjetpackcompose.data.util.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.data.mapper.toMediaItem
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.util.onDefaultDispatcher
import com.example.mediaplayerjetpackcompose.data.util.onIoDispatcher
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.PagerThumbnailModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPageViewModel(
  private var musicMediaStoreRepository: MediaStoreRepositoryImpl<MusicModel>,
  private var musicServiceConnection: MusicServiceConnection,
  private var dataBaseDao: DataBaseDao,
  private var mediaThumbnailUtil: MediaThumbnailUtil,
  private var deviceVolumeManager: DeviceVolumeManager,
) : ViewModel() {

  private var originalMusicList = mutableStateListOf<MusicModel>()

  var musicList = mutableStateListOf<MusicModel>()

  var artistsMusicMap = mutableStateListOf<CategoryMusicModel>()
  var albumMusicMap = mutableStateListOf<CategoryMusicModel>()
  var folderMusicMap = mutableStateListOf<CategoryMusicModel>()

  var musicArtworkColorPalette by mutableIntStateOf(MediaThumbnailUtil.DefaultColorPalette)

  var isLoading by mutableStateOf(true)

  var sortState by mutableStateOf(SortState(SortTypeModel.NAME, false))
  var currentTabState by mutableStateOf(TabBarPosition.MUSIC)

  var isFullPlayerShow by mutableStateOf(false)

  var currentPagerPage = mutableIntStateOf(0)

  var pagerItemList = mutableStateListOf<PagerThumbnailModel>()

  var currentMusicState = musicServiceConnection.currentMediaState
    .stateIn(
      viewModelScope,
      SharingStarted.Eagerly,
      CurrentMediaState.Empty,
    )

  var favoriteMusic = dataBaseDao.getAllFaFavoriteSongs()
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

  private var _currentMusicPosition = MutableStateFlow(0L)
  var currentMusicPosition =
    _currentMusicPosition
      .onStart {
        musicServiceConnection
          .musicPosition
          .onEach {
            _currentMusicPosition.value = it
          }.launchIn(viewModelScope)
      }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        0L
      )

  var currentDeviceVolume = deviceVolumeManager.currentMusicLevelVolume
    .onStart {
      deviceVolumeManager.registerContentObserver()
    }.stateIn(
      viewModelScope,
      SharingStarted.Eagerly,
      0,
    )

  init {
    getMusic()
  }

  fun onPlayerAction(action: PlayerActions) {
    when (action) {
      PlayerActions.MoveNextPlayer -> moveToNext()
      PlayerActions.PausePlayer -> pauseMusic()
      PlayerActions.ResumePlayer -> resumeMusic()
      is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
      is PlayerActions.SeekTo -> seekToPosition(action.value)
      is PlayerActions.OnRepeatMode -> setRepeatMode(action.value)
      is PlayerActions.MovePreviousPlayer -> moveToPrevious(action.seekToStart)
      is PlayerActions.OnMoveToIndex -> moveToMediaIndex(index = action.value)
    }
  }

  fun setDeviceVolume(volume: Float) {
    deviceVolumeManager.setVolume(volume)
  }

  fun getMaxDeviceVolume(): Int = deviceVolumeManager.getMaxVolume()

  private fun moveToMediaIndex(index: Int) {
    _currentMusicPosition.update { 0 }

    musicServiceConnection.mediaController?.apply {
      seekTo(index, 0L)
      prepare()
      play()
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
      updateMediaItemListAfterSort(list)
    }
    return list
  }

  private suspend fun updateMediaItemListAfterSort(list: List<MusicModel>) =
    withContext(Dispatchers.Default) {
      val index = list.indexOfFirst { it.musicId.toString() == currentMusicState.value.mediaId }

      val pagerItem =
        list.map { PagerThumbnailModel(uri = it.artworkUri, musicId = it.musicId, name = it.name, artist = it.artist) }

      viewModelScope.launch {
        currentPagerPage.intValue = if (index == -1) 0 else index
        pagerItemList.clear()
        pagerItemList.addAll(pagerItem)
        musicServiceConnection.mediaController?.setMediaItems(
          musicList.map(MusicModel::toMediaItem), if (index == -1) -1 else index, _currentMusicPosition.value
        )
      }
    }

  private fun moveToNext() {
    val hasNextItem = musicServiceConnection.mediaController?.hasNextMediaItem() ?: false
    if (!hasNextItem) return
    musicServiceConnection.mediaController?.apply {
      seekToNext()
      prepare()
      play()
    }
    _currentMusicPosition.update { 0 }
    currentPagerPage.intValue += 1
  }

  private fun moveToPrevious(seekToStart: Boolean = false) {
    val hasPreviewItem = musicServiceConnection.mediaController?.hasNextMediaItem() ?: false
    if (!hasPreviewItem) return

    if (_currentMusicPosition.value <= 15_000 || seekToStart) {
      musicServiceConnection.mediaController?.apply {
        seekToPreviousMediaItem()
        prepare()
        play()
      }
      currentPagerPage.intValue -= 1
    } else musicServiceConnection.mediaController?.apply {
      seekToPrevious()
      prepare()
      play()
    }
    _currentMusicPosition.update { 0 }
  }

  private fun pauseMusic() = musicServiceConnection.mediaController?.pause()

  private fun resumeMusic() = musicServiceConnection.mediaController?.play()

  private fun seekToPosition(position: Long) {
    musicServiceConnection.mediaController?.seekTo(position)
    _currentMusicPosition.update { position }
  }

  private fun setRepeatMode(repeatMode: Int) {
    musicServiceConnection.mediaController?.repeatMode = repeatMode
  }

  private fun handleFavoriteSongs(mediaId: String) {
    viewModelScope.launch {
      onIoDispatcher {
        if (mediaId in favoriteMusic.value.map { it.mediaId }) {
          dataBaseDao.deleteFavoriteSong(mediaId)
        } else {
          dataBaseDao.insertFavoriteSong(FavoriteMusicModel(mediaId = mediaId))
        }
      }
    }
  }

  fun searchMusic(input: String) = viewModelScope.launch(Dispatchers.Default) {
    if (input.isNotEmpty() || input.isNotEmpty()) {
      val filteredList = originalMusicList.filter { it.name.lowercase().contains(input.lowercase()) }
      viewModelScope.launch {
        musicList.clear()
        musicList.addAll(filteredList)
      }
    } else {
      viewModelScope.launch {
        musicList.clear()
        musicList.addAll(originalMusicList)
      }
    }
  }

  fun playMusic(index: Int, musicList: List<MusicModel>) = viewModelScope.launch(Dispatchers.Default) {
    val pagerItem = musicList.map { PagerThumbnailModel(uri = it.artworkUri, musicId = it.musicId, name = it.name, artist = it.artist) }
    val musicItem = musicList.map(MusicModel::toMediaItem)
    viewModelScope.launch {
      pagerItemList.clear()
      pagerItemList.addAll(pagerItem)
      currentPagerPage.intValue = index
      musicServiceConnection.mediaController?.run {
        setMediaItems(musicItem, index, 0L)
        prepare()
        play()
      }
    }
  }

  private fun getMusic() = viewModelScope.launch {
    musicMediaStoreRepository.getMedia()
      .collect { result ->
        when (result) {

          MediaStoreResult.Loading -> isLoading = true

          is MediaStoreResult.Result -> {
            viewModelScope.launch(Dispatchers.Default) {
              val mainList = result.result
              val pagerItem =
                result.result.map { PagerThumbnailModel(uri = it.artworkUri, musicId = it.musicId, name = it.name, artist = it.artist) }
              val artistItem = result.result.groupBy { by -> by.artist }.map { CategoryMusicModel(it.key, it.value) }
              val albumItem = result.result.groupBy { by -> by.album }.map { CategoryMusicModel(it.key, it.value) }
              val folderItem = result.result.groupBy { by -> by.folderName }.map { CategoryMusicModel(it.key, it.value) }
              val currentPagerIndex = if (currentMusicState.value.mediaId.isNotEmpty()) {
                mainList.indexOfFirst { it.musicId == currentMusicState.value.mediaId.toLong() }
              } else 0

              viewModelScope.launch {
                currentPagerPage.intValue = currentPagerIndex
                musicList.addAll(mainList)
                pagerItemList.addAll(pagerItem)
                originalMusicList.addAll(mainList)
                artistsMusicMap.addAll(artistItem)
                albumMusicMap.addAll(albumItem)
                folderMusicMap.addAll(folderItem)
                isLoading = false
              }
            }

          }

          else -> {}
        }
      }

  }

  override fun onCleared() {
    super.onCleared()
    deviceVolumeManager.unRegisterContentObserver()
  }

}