package com.example.mediaplayerjetpackcompose.presentation.screen.music

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaplayerjetpackcompose.data.musicManager.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import com.example.mediaplayerjetpackcompose.data.musicManager.SearchMusicManager
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.util.DeviceVolumeManager
import com.example.mediaplayerjetpackcompose.data.util.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.data.util.sortMusic
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.share.MediaPlayerState
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicPageViewModel(
  private var musicServiceConnection: MusicServiceConnection,
  private var mediaThumbnailUtil: MediaThumbnailUtil,
  private var deviceVolumeManager: DeviceVolumeManager,
  private var musicSource: MusicSource,
  private var searchMusicManager: SearchMusicManager,
  private var favoriteMusicManager: FavoriteMusicManager,
) : ViewModel() {

  var musicList = mutableStateListOf<MusicModel>()

  var musicArtworkColorPalette by mutableIntStateOf(MediaThumbnailUtil.DefaultColorPalette)

  var tabBarState by mutableStateOf(TabBarModel.All)

  var isLoading by mutableStateOf(true)

  private var _sortSate = MutableStateFlow(SortState(SortTypeModel.NAME, false))
  var sortState = _sortSate.asStateFlow()

  var currentMusicState = musicServiceConnection.mediaPlayerState
    .stateIn(
      viewModelScope,
      SharingStarted.Eagerly,
      MediaPlayerState.Empty,
    )

  var artworkPagerList = musicServiceConnection.artworkPagerList.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    emptyList(),
  )

  var favoriteSongsMediaId = favoriteMusicManager.favoriteMusicMediaIdList.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var currentArtworkPagerIndex = musicServiceConnection.currentArtworkPagerIndex.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0,
  )

  var favoriteSongsList = musicSource.favoriteSongs().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var searchList = searchMusicManager.searchResult.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
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

  var album = musicSource.album().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var artist = musicSource.artist().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var folder = musicSource.folder().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
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

  fun sortMusicListByCategory() {
    viewModelScope.launch {
      var sortedList = sortMusic(list = musicList, isDescending = _sortSate.value.isDec, sortBy = _sortSate.value.sortType)
      musicList.apply {
        clear()
        addAll(sortedList)
      }
    }
  }

  private fun handleFavoriteSongs(mediaId: String) {
    viewModelScope.launch {
      favoriteMusicManager.handleFavoriteSongs(mediaId)
    }
  }

  fun searchMusic(input: String) = viewModelScope.launch(Dispatchers.Default) {
    searchMusicManager.search(input)
  }

  fun playMusic(index: Int, musicList: List<MusicModel>) = viewModelScope.launch {
    musicServiceConnection.playSongs(index, musicList)
  }

  fun getMusic() = viewModelScope.launch {
    musicSource.songs.collect { result ->
      musicList = result.toMutableStateList()
      isLoading = false
    }
  }

  override fun onCleared() {
    super.onCleared()
    deviceVolumeManager.unRegisterContentObserver()
  }

}