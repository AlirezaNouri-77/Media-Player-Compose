package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.util.DeviceVolumeManager
import com.example.mediaplayerjetpackcompose.data.util.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryLists
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.CategoryMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.filterList
import kotlin.collections.map

class MusicPageViewModel(
  private var musicMediaStoreRepository: MediaStoreRepositoryImpl<MusicModel>,
  private var musicServiceConnection: MusicServiceConnection,
  private var dataBaseDao: DataBaseDao,
  private var mediaThumbnailUtil: MediaThumbnailUtil,
  private var deviceVolumeManager: DeviceVolumeManager,
) : ViewModel() {

  private var originalMusicList = mutableStateListOf<MusicModel>()

  var musicList = mutableStateListOf<MusicModel>()

  var searchList by mutableStateOf(emptyList<MusicModel>())

  var categoryLists by mutableStateOf(CategoryLists.Empty)

  var musicArtworkColorPalette by mutableIntStateOf(MediaThumbnailUtil.DefaultColorPalette)

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  var artworkPagerList = musicServiceConnection.artworkPagerList.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    emptyList(),
  )

  var currentArtworkPagerIndex = musicServiceConnection.currentArtworkPagerIndex.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0,
  )

  private var _sortSate = MutableStateFlow(SortState(SortTypeModel.NAME, false))
  var sortState = _sortSate.asStateFlow()

  var currentMusicState = musicServiceConnection.mediaPlayerState
    .stateIn(
      viewModelScope,
      SharingStarted.Eagerly,
      MediaPlayerState.Empty,
    )

  var favoriteMusicMediaId = dataBaseDao.getAllFaFavoriteSongs()
    .map { it.map { it.mediaId } }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      emptyList()
    )

  var currentMusicPosition = musicServiceConnection.currentMusicPosition.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0L
  )

  var currentDeviceVolume = deviceVolumeManager.currentMusicLevelVolume
    .onStart {
      deviceVolumeManager.registerContentObserver()
      deviceVolumeManager.setInitialVolume()
    }
    .stateIn(
      viewModelScope,
      SharingStarted.Eagerly,
      0,
    )

  init {
    getMusic()
  }

  fun onPlayerAction(action: PlayerActions) {
    when (action) {
      PlayerActions.MoveNextPlayer -> musicServiceConnection.moveToNext()
      PlayerActions.PausePlayer -> musicServiceConnection.pauseMusic()
      PlayerActions.ResumePlayer -> musicServiceConnection.resumeMusic()
      is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
      is PlayerActions.SeekTo -> musicServiceConnection.seekToPosition(action.value)
      is PlayerActions.OnRepeatMode -> musicServiceConnection.setRepeatMode(action.value)
      is PlayerActions.MovePreviousPlayer -> musicServiceConnection.moveToPrevious(action.seekToStart)
      is PlayerActions.OnMoveToIndex -> musicServiceConnection.moveToMediaIndex(index = action.value)
      is PlayerActions.UpdateArtworkPageIndex -> musicServiceConnection.setCurrentArtworkPagerIndex(action.value)
    }
  }

  fun updateSortType(input: SortTypeModel) = _sortSate.update { it.copy(sortType = input) }

  fun updateSortIsDec(input: Boolean) = _sortSate.update { it.copy(isDec = input) }

  fun setDeviceVolume(volume: Float) = deviceVolumeManager.setVolume(volume)

  fun getMaxDeviceVolume(): Int = deviceVolumeManager.getMaxVolume()

  fun getColorPaletteFromArtwork(uri: Uri) {
    viewModelScope.launch {
      val bitmap = mediaThumbnailUtil.getMusicArt(uri)
      musicArtworkColorPalette = MediaThumbnailUtil.getMainColorOfBitmap(bitmap)
    }
  }

  fun sortMusicListByCategory(
    list: MutableList<MusicModel>
  ) {
    viewModelScope.launch {
      var sortedList = withContext(Dispatchers.Default) {
        return@withContext when (_sortSate.value.sortType) {
          SortTypeModel.NAME -> if (_sortSate.value.isDec) list.sortedByDescending { it.name } else list.sortedBy { it.name }
          SortTypeModel.ARTIST -> if (_sortSate.value.isDec) list.sortedByDescending { it.artist } else list.sortedBy { it.artist }
          SortTypeModel.DURATION -> if (_sortSate.value.isDec) list.sortedByDescending { it.duration } else list.sortedBy { it.duration }
          SortTypeModel.SIZE -> if (_sortSate.value.isDec) list.sortedByDescending { it.size } else list.sortedBy { it.size }
        }
      }

      musicList.apply {
        clear()
        addAll(sortedList)
      }

    }
  }

  private fun handleFavoriteSongs(mediaId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val isMediaIdInDatabase = favoriteMusicMediaId.value.firstOrNull { it == mediaId }
      if (isMediaIdInDatabase != null) {
        dataBaseDao.deleteFavoriteSong(mediaId)
      } else {
        dataBaseDao.insertFavoriteSong(FavoriteMusicModel(mediaId = mediaId))
      }
    }
  }

  fun searchMusic(input: String) = viewModelScope.launch(Dispatchers.Default) {
    if (input.isNotEmpty() || input.isNotBlank()) {
      val filteredList = originalMusicList.filterList { name.lowercase().contains(input.lowercase()) }
      viewModelScope.launch {
        searchList = filteredList
      }
    } else {
      viewModelScope.launch {
        searchList = originalMusicList
      }
    }
  }

  fun playMusic(index: Int, musicList: List<MusicModel>) = viewModelScope.launch {
    musicServiceConnection.playSongs(index, musicList)
  }

  fun getMusic() = viewModelScope.launch {
    musicMediaStoreRepository.getMedia()
      .collect { result ->
        when (result) {

          MediaStoreResult.Loading -> isLoading = true

          is MediaStoreResult.Result -> {
            viewModelScope.launch(Dispatchers.Default) {
              val mainList = result.result
              val artistItem = result.result.groupBy { by -> by.artist }.map { CategoryMusicModel(it.key, it.value) }
              val albumItem = result.result.groupBy { by -> by.album }.map { CategoryMusicModel(it.key, it.value) }
              val folderItem = result.result.groupBy { by -> by.folderName }.map { CategoryMusicModel(it.key, it.value) }

              viewModelScope.launch {
                musicList.addAll(mainList)
                originalMusicList.addAll(mainList)
                categoryLists = categoryLists.copy(artist = artistItem, album = albumItem, folder = folderItem)
              }
            }

          }

          else -> {}
        }
      }
    isLoading = false
  }

  override fun onCleared() {
    super.onCleared()
    deviceVolumeManager.unRegisterContentObserver()
  }

}