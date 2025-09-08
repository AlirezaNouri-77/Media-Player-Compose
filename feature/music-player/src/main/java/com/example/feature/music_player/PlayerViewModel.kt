package com.example.feature.music_player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.MusicThumbnailUtilImpl
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.model.MusicModel
import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.util.DeviceVolumeManager
import com.example.feature.music_player.model.PlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private var musicServiceConnection: MusicServiceConnection,
    private var mediaThumbnailUtil: MusicThumbnailUtilImpl,
    private var deviceVolumeManager: DeviceVolumeManager,
    private var favoriteMusicSource: FavoriteRepositoryImpl,
) : ViewModel() {
    private var _uiState = MutableStateFlow(PlayerUiState())
    val playerUiState = _uiState.onStart {
        observePlayerStates()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        PlayerUiState(),
    )

    private fun observePlayerStates() {
        deviceVolumeManager.volumeChangeListener().onEach {
            _uiState.update { uiState -> uiState.copy(currentDeviceVolume = it) }
        }.launchIn(viewModelScope)
        musicServiceConnection.currentArtworkPagerIndex.onEach {
            _uiState.update { uiState -> uiState.copy(currentThumbnailPagerIndex = it) }
        }.launchIn(viewModelScope)
        musicServiceConnection.artworkPagerList.onEach {
            _uiState.update { uiState -> uiState.copy(thumbnailsList = it) }
        }.launchIn(viewModelScope)
        musicServiceConnection.playerState.onEach {
            _uiState.update { uiState -> uiState.copy(currentPlayerState = it) }
        }.launchIn(viewModelScope)
        musicServiceConnection.currentPlayerPosition.onEach {
            _uiState.update { uiState -> uiState.copy(currentPlayerPosition = it) }
        }.launchIn(viewModelScope)
    }

    fun onPlayerAction(action: PlayerActions) {
        when (action) {
            PlayerActions.MoveNextPlayer -> musicServiceConnection.moveToNext()
            PlayerActions.PausePlayer -> musicServiceConnection.pauseMusic()
            PlayerActions.ResumePlayer -> musicServiceConnection.resumeMusic()
            is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
            is PlayerActions.PlaySongs -> playMusic(action.index, action.list)
            is PlayerActions.SeekTo -> {
                _uiState.update { it.copy(currentPlayerPosition = action.value) }
                musicServiceConnection.seekToPosition(action.value)
            }

            is PlayerActions.OnRepeatMode -> musicServiceConnection.setRepeatMode(action.value)
            is PlayerActions.MovePreviousPlayer -> musicServiceConnection.moveToPrevious(
                action.seekToStart,
                _uiState.value.currentPlayerPosition,
            )

            is PlayerActions.OnMoveToIndex -> musicServiceConnection.moveToMediaIndex(index = action.value)
            is PlayerActions.UpdateArtworkPageIndex -> musicServiceConnection.setCurrentArtworkPagerIndex(
                action.value,
            )
        }
    }

    fun setDeviceVolume(volume: Float) {
        deviceVolumeManager.setVolume(volume)
        _uiState.update { it.copy(currentDeviceVolume = volume.toInt()) }
    }

    fun getMaxDeviceVolume(): Int = deviceVolumeManager.getMaxVolume()

    fun getColorPaletteFromArtwork(uri: Uri) {
        viewModelScope.launch {
            val bitmap = mediaThumbnailUtil.getMusicThumbnail(uri)
            _uiState.update {
                it.copy(thumbnailDominantColor = mediaThumbnailUtil.getMainColorOfBitmap(bitmap))
            }
        }
    }

    private fun handleFavoriteSongs(mediaId: String) {
        viewModelScope.launch {
            val update = favoriteMusicSource.handleFavoriteSongs(mediaId)
            musicServiceConnection.updateCurrentMusicFavorite(update)
        }
    }

    private fun playMusic(index: Int, musicList: List<MusicModel>) =
        musicServiceConnection.playSongs(index, musicList)

    override fun onCleared() {
        super.onCleared()
        // deviceVolumeManager.unRegisterContentObserver()
    }
}
