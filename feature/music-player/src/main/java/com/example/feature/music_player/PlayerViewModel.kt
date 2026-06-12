package com.example.feature.music_player

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.MusicThumbnailUtilImpl
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.model.MusicModel
import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.util.DeviceVolumeManager
import com.example.feature.music_player.model.PlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val mediaThumbnailUtil: MusicThumbnailUtilImpl,
    private val deviceVolumeManager: DeviceVolumeManager,
    private val favoriteMusicSource: FavoriteRepositoryImpl,
) : ViewModel() {
    private var _uiState = MutableStateFlow(PlayerUiState())
    val playerUiState = _uiState.onStart {
        observePlayerStates()
        updatePagerItem()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _uiState.value,
    )

    private fun observePlayerStates() {
        deviceVolumeManager.volumeChangeListener().onEach {
            _uiState.update { uiState -> uiState.copy(currentDeviceVolume = it) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            musicServiceConnection.currentPlayedMusicList.collectLatest {
                _uiState.update { uiState -> uiState.copy(playedMusicList = it) }
            }
        }

        viewModelScope.launch {
            musicServiceConnection.playerState.collectLatest {
                if (it.currentMediaInfo.musicID != _uiState.value.currentPlayerState.currentMediaInfo.musicID) {
                    getColorPaletteFromArtwork(it.currentMediaInfo.musicUri.toUri())
                }
                _uiState.update { uiState -> uiState.copy(currentPlayerState = it) }
            }
        }

        musicServiceConnection.currentPlayerPosition.onEach {
            _uiState.update { uiState -> uiState.copy(currentPlayerPosition = it) }
        }.launchIn(viewModelScope)
    }

    fun onPlayerAction(action: PlayerActions) {
        when (action) {
            PlayerActions.MoveNextPlayer -> musicServiceConnection.moveToNext()
            PlayerActions.PausePlayer -> musicServiceConnection.pauseMusic()
            PlayerActions.ResumePlayer -> musicServiceConnection.resumeMusic()
            PlayerActions.onShuffleMode -> {
                musicServiceConnection.setShuffleMode(!playerUiState.value.currentPlayerState.isShuffleMode)
                updatePagerItem()
            }

            is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
            is PlayerActions.PlaySongs -> playMusic(action.index, action.list)
            is PlayerActions.SeekTo -> {
                _uiState.update { it.copy(currentPlayerPosition = action.value) }
                musicServiceConnection.seekToPosition(action.value)
            }

            is PlayerActions.OnRepeatMode -> {
                musicServiceConnection.setRepeatMode(action.value)
                updatePagerItem()
            }

            is PlayerActions.MovePreviousPlayer -> musicServiceConnection.moveToPrevious(
                action.seekToStart,
                _uiState.value.currentPlayerPosition,
            )

            is PlayerActions.OnMoveToIndex -> {
                val index =
                    playerUiState.value.playedMusicList.indexOfFirst { action.musicId.toLong() == it.musicId }
                musicServiceConnection.moveToMediaIndex(index = index)
            }

            is PlayerActions.UpdateArtworkPageIndex -> {
                val targetIndex = playerUiState.value.thumbnailsList.indexOfFirst {
                    it.musicId == playerUiState.value.currentPlayerState.currentMediaInfo.musicID
                }
                if (targetIndex > -1) {
                    _uiState.update { uiState -> uiState.copy(currentThumbnailPagerIndex = targetIndex) }
                }
            }
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

    private fun updatePagerItem() {
        val list = musicServiceConnection.getMediaItemsList()
        if (list.isEmpty()) return
        val index = list.indexOfFirst { it.musicId == _uiState.value.currentPlayerState.currentMediaInfo.musicID }
        _uiState.update { uiState ->
            uiState.copy(
                thumbnailsList = list,
                currentThumbnailPagerIndex = if (index != -1) index else uiState.currentThumbnailPagerIndex,
            )
        }
    }

    private fun handleFavoriteSongs(mediaId: String) {
        viewModelScope.launch {
            val update = favoriteMusicSource.handleFavoriteSongs(mediaId)
            musicServiceConnection.updateCurrentMusicFavorite(update)
        }
    }

    private fun playMusic(index: Int, musicList: List<MusicModel>) {
        musicServiceConnection.playSongs(index, musicList)
        _uiState.update { it.copy(playedMusicList = musicList) }
        if (playerUiState.value.currentPlayerState.isShuffleMode) {
            musicServiceConnection.setShuffleMode(!playerUiState.value.currentPlayerState.isShuffleMode)
        }
        updatePagerItem()
    }
}
