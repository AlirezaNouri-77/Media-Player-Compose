package com.shermanrex.feature.music_player

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shermanrex.core.domain.MusicThumbnailUtilImpl
import com.shermanrex.core.domain.respository.FavoriteRepositoryImpl
import com.shermanrex.core.model.MusicModel
import com.shermanrex.core.model.PlayerTimers
import com.shermanrex.core.model.toId
import com.shermanrex.core.music_media3.MusicServiceConnection
import com.shermanrex.core.music_media3.util.DeviceVolumeManager
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
    private var _playerUiState = MutableStateFlow(PlayerUiState())
    val playerUiState = _playerUiState.onStart {
        observePlayerStates()
        observePlayerTimerStates()
        updatePagerItem()
        observerCurrentPlayerIsFavorite()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _playerUiState.value,
    )

    fun onPlayerAction(action: PlayerActions) {
        when (action) {
            PlayerActions.MoveNextPlayer -> musicServiceConnection.moveToNext()
            PlayerActions.PausePlayer -> musicServiceConnection.pauseMusic()
            PlayerActions.ResumePlayer -> musicServiceConnection.resumeMusic()
            PlayerActions.OnShuffleMode -> {
                musicServiceConnection.setShuffleMode(!playerUiState.value.currentPlayerState.isShuffleMode)
                updatePagerItem()
            }

            is PlayerActions.OnFavoriteToggle -> handleFavoriteSongs(action.mediaId)
            is PlayerActions.PlaySongs -> playMusic(action.index, action.list)
            is PlayerActions.SeekTo -> {
                _playerUiState.update { it.copy(currentPlayerPosition = action.value) }
                musicServiceConnection.seekToPosition(action.value)
            }

            is PlayerActions.OnRepeatMode -> {
                musicServiceConnection.setRepeatMode(action.value.toId())
                updatePagerItem()
            }

            is PlayerActions.MovePreviousPlayer ->
                musicServiceConnection.moveToPrevious(action.seekToStart, _playerUiState.value.currentPlayerPosition)

            is PlayerActions.OnMoveToIndex -> {
                val index = playerUiState.value.playedMusicList.indexOfFirst { action.musicId.toLong() == it.musicId }
                musicServiceConnection.moveToMediaIndex(index = index)
            }

            is PlayerActions.UpdateArtworkPageIndex -> {
                val targetIndex = playerUiState.value.thumbnailsList.indexOfFirst {
                    it.musicId == playerUiState.value.currentPlayerState.playingMusicInfo.musicID
                }
                if (targetIndex > -1) {
                    _playerUiState.update { uiState -> uiState.copy(currentThumbnailPagerIndex = targetIndex) }
                }
            }

            PlayerActions.OnShowTimerBottomSheet ->
                _playerUiState.update { it.copy(shouldShowTimerBottomSheet = true) }

            PlayerActions.OnHideTimerBottomSheet ->
                _playerUiState.update { it.copy(shouldShowTimerBottomSheet = false) }

            is PlayerActions.OnTimerClick -> handleOnTimer(action.timers)
        }
    }

    private fun observePlayerStates() {
        deviceVolumeManager.volumeChangeListener.onEach {
            _playerUiState.update { uiState -> uiState.copy(currentDeviceVolume = it) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            musicServiceConnection.currentPlayedMusicList.collectLatest {
                _playerUiState.update { uiState -> uiState.copy(playedMusicList = it) }
            }
        }

        viewModelScope.launch {
            musicServiceConnection.playerState.collectLatest {
                if (it.playingMusicInfo.musicID != _playerUiState.value.currentPlayerState.playingMusicInfo.musicID) {
                    setColorPaletteFromArtwork(it.playingMusicInfo.musicUri.toUri())
                }
                _playerUiState.update { uiState -> uiState.copy(currentPlayerState = it) }
            }
        }

        musicServiceConnection.currentPlayerPosition.onEach {
            _playerUiState.update { uiState -> uiState.copy(currentPlayerPosition = it) }
        }.launchIn(viewModelScope)
    }

    private fun observePlayerTimerStates() {
        musicServiceConnection.playerTimerState.onEach {
            _playerUiState.update { uiState -> uiState.copy(playerTimerState = it) }
        }.launchIn(viewModelScope)
    }

    fun setDeviceVolume(volume: Float) {
        deviceVolumeManager.setVolume(volume)
        _playerUiState.update { it.copy(currentDeviceVolume = volume.toInt()) }
    }

    fun getMaxDeviceVolume(): Int = deviceVolumeManager.getMaxVolume()

    private fun handleOnTimer(timers: PlayerTimers) {
        if (playerUiState.value.playerTimerState.playerTimerState == PlayerTimers.INITIAL) {
            if (timers == PlayerTimers.END_OFF_SONG) {
                musicServiceConnection.setPlayerTimer(timers, true)
            } else {
                musicServiceConnection.startPlayerTimer(timers.time)
                musicServiceConnection.setPlayerTimer(timers)
            }
        } else {
            musicServiceConnection.stopPlayerTimer()
            musicServiceConnection.setPlayerTimer(PlayerTimers.INITIAL)
        }
    }

    private fun setColorPaletteFromArtwork(uri: Uri) {
        viewModelScope.launch {
            val color: Int = mediaThumbnailUtil.getMainColorOfBitmap(uri)
            _playerUiState.update { it.copy(thumbnailDominantColor = color) }
        }
    }

    private fun updatePagerItem() {
        val list = musicServiceConnection.getMediaItemsList()
        if (list.isEmpty()) return
        val index =
            list.indexOfFirst { it.musicId == _playerUiState.value.currentPlayerState.playingMusicInfo.musicID }
        _playerUiState.update { uiState ->
            uiState.copy(
                thumbnailsList = list,
                currentThumbnailPagerIndex = if (index != -1) index else uiState.currentThumbnailPagerIndex,
            )
        }
    }

    private fun observerCurrentPlayerIsFavorite() {
        viewModelScope.launch {
            combine(
                playerUiState,
                favoriteMusicSource.favoritesMediaIdList(),
            ) { playerState, favoriteList ->
                playerState.currentPlayerState.playingMusicInfo.musicID in favoriteList
            }.collectLatest { isFavorite ->
                _playerUiState.update { it.copy(currentPlayerState = playerUiState.value.currentPlayerState.copy(isFavorite = isFavorite)) }
            }
        }
    }

    private fun handleFavoriteSongs(mediaId: String) = viewModelScope.launch { favoriteMusicSource.handleFavoriteSongs(mediaId) }

    private fun playMusic(index: Int, musicList: List<MusicModel>) {
        musicServiceConnection.playSongs(index, musicList)
        _playerUiState.update { it.copy(playedMusicList = musicList) }
        if (playerUiState.value.currentPlayerState.isShuffleMode) {
            musicServiceConnection.setShuffleMode(!playerUiState.value.currentPlayerState.isShuffleMode)
        }
        updatePagerItem()
    }
}
