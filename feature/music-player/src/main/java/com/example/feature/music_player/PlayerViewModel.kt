package com.example.feature.music_player

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.util.MusicThumbnailUtil
import com.example.core.domain.respository.MusicThumbnailUtilImpl
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.model.MusicModel
import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.model.PlayerStateModel
import com.example.core.music_media3.util.DeviceVolumeManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
  private var musicServiceConnection: MusicServiceConnection,
  private var mediaThumbnailUtil: MusicThumbnailUtilImpl,
  private var deviceVolumeManager: DeviceVolumeManager,
  private var favoriteMusicSource: FavoriteRepositoryImpl,
) : ViewModel() {

  var musicArtworkColorPalette by mutableIntStateOf(MusicThumbnailUtil.DEFAULT_COLOR_PALETTE)

  var currentMusicState = musicServiceConnection.playerState
    .stateIn(
      viewModelScope,
      SharingStarted.Eagerly,
      PlayerStateModel.Empty,
    )

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

  private var _currentMusicPosition = MutableStateFlow(0L)
  var currentMusicPosition = _currentMusicPosition.asStateFlow()
    .onStart {
      viewModelScope.launch {
        while (currentCoroutineContext().isActive) {
          if (currentMusicState.value.isPlaying) {
            val position = musicServiceConnection.currentPlayerPosition()
            _currentMusicPosition.value = position
          }
          delay(80L)
        }
      }
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      0L,
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

  fun onPlayerAction(action: PlayerActions) {
    when (action) {
      PlayerActions.MoveNextPlayer -> musicServiceConnection.moveToNext()
      PlayerActions.PausePlayer -> musicServiceConnection.pauseMusic()
      PlayerActions.ResumePlayer -> musicServiceConnection.resumeMusic()
      is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
      is PlayerActions.PlaySongs -> playMusic(action.index, action.list)
      is PlayerActions.SeekTo -> {
        _currentMusicPosition.value = action.value
        musicServiceConnection.seekToPosition(action.value)
      }
      is PlayerActions.OnRepeatMode -> musicServiceConnection.setRepeatMode(action.value)
      is PlayerActions.MovePreviousPlayer -> musicServiceConnection.moveToPrevious(action.seekToStart, currentMusicPosition.value)
      is PlayerActions.OnMoveToIndex -> musicServiceConnection.moveToMediaIndex(index = action.value)
      is PlayerActions.UpdateArtworkPageIndex -> musicServiceConnection.setCurrentArtworkPagerIndex(action.value)
    }
  }

  fun setDeviceVolume(volume: Float) = deviceVolumeManager.setVolume(volume)

  fun getMaxDeviceVolume(): Int = deviceVolumeManager.getMaxVolume()

  fun getColorPaletteFromArtwork(uri: Uri) {
    viewModelScope.launch {
      val bitmap = mediaThumbnailUtil.getMusicThumbnail(uri)
      musicArtworkColorPalette = mediaThumbnailUtil.getMainColorOfBitmap(bitmap)
    }
  }

  private fun handleFavoriteSongs(mediaId: String) {
    viewModelScope.launch {
      val update = favoriteMusicSource.handleFavoriteSongs(mediaId)
      musicServiceConnection.updateCurrentMusicFavorite(update)
    }
  }

  private fun playMusic(index: Int, musicList: List<MusicModel>) = musicServiceConnection.playSongs(index, musicList)

  override fun onCleared() {
    super.onCleared()
    deviceVolumeManager.unRegisterContentObserver()
  }

}