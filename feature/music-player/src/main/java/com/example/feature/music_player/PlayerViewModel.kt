package com.example.feature.music_player

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.PlayerStateModel
import com.example.core.music_media3.util.DeviceVolumeManager
import com.example.core.music_media3.util.MusicThumbnailUtil
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.model.MusicModel
import com.example.core.music_media3.util.MusicThumbnailUtilImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
  private var musicServiceConnection: MusicServiceConnection,
  private var mediaThumbnailUtil: MusicThumbnailUtilImpl,
  private var deviceVolumeManager: DeviceVolumeManager,
  private var favoriteMusicSource: FavoriteMusicSourceImpl,
) : ViewModel() {

  var miniPlayerHeight = 70.dp

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

  var favoriteSongsMediaId = favoriteMusicSource.favoriteMusicMediaIdList.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000L),
    emptyList(),
  )

  var currentArtworkPagerIndex = musicServiceConnection.currentArtworkPagerIndex.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0,
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

  fun onPlayerAction(action: PlayerActions) {
    when (action) {
      PlayerActions.MoveNextPlayer -> musicServiceConnection.moveToNext()
      PlayerActions.PausePlayer -> musicServiceConnection.pauseMusic()
      PlayerActions.ResumePlayer -> musicServiceConnection.resumeMusic()
      is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
      is PlayerActions.PlaySongs -> playMusic(action.index, action.list)
      is PlayerActions.SeekTo -> musicServiceConnection.seekToPosition(action.value)
      is PlayerActions.OnRepeatMode -> musicServiceConnection.setRepeatMode(action.value)
      is PlayerActions.MovePreviousPlayer -> musicServiceConnection.moveToPrevious(action.seekToStart)
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
      favoriteMusicSource.handleFavoriteSongs(mediaId)
    }
  }

  private fun playMusic(index: Int, musicList: List<MusicModel>) = viewModelScope.launch {
    musicServiceConnection.playSongs(index, musicList)
  }

  override fun onCleared() {
    super.onCleared()
    deviceVolumeManager.unRegisterContentObserver()
  }

}