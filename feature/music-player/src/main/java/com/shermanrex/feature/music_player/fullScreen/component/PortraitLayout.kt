package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.PlayerActions
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PortraitLayout(
    modifier: Modifier = Modifier,
    playerUiState: PlayerUiState,
    pagerMusicList: ImmutableList<ArtworkModel>,
    onBack: () -> Unit,
    onPlayerAction: (PlayerActions) -> Unit,
    setCurrentPagerNumber: (Int) -> Unit,
    maxDeviceVolume: Int,
    currentVolume: Int,
    onVolumeChange: (Float) -> Unit,
    clickOnArtist: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            )
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBack,
            onTimerClick = {
                onPlayerAction(PlayerActions.OnShowTimerBottomSheet)
            },
        )
        FullscreenPlayerPager(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            pagerItem = pagerMusicList,
            currentPagerPage = playerUiState.currentThumbnailPagerIndex,
            currentMusicID = playerUiState.currentPlayerState.playingMusicInfo.musicID.toLong(),
            onPlayerAction = { onPlayerAction(it) },
            setCurrentPagerNumber = { setCurrentPagerNumber(it) },
        )
        SongDetail(
            modifier = Modifier.padding(horizontal = 12.dp),
            onArtistClick = clickOnArtist,
            isFavorite = playerUiState.currentPlayerState.isFavorite,
            playerUiState = playerUiState,
            onFavoriteClick = {
                onPlayerAction(PlayerActions.OnFavoriteToggle(playerUiState.currentPlayerState.playingMusicInfo.musicID))
            },
        )
        SliderSection(
            modifier = Modifier.padding(horizontal = 12.dp),
            currentMusicPosition = playerUiState.currentPlayerPosition,
            seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
            duration = playerUiState.currentPlayerState.playingMusicInfo.duration.toFloat(),
        )
        SongController(
            isPlaying = playerUiState.currentPlayerState.isPlaying,
            playerRepeatMode = playerUiState.currentPlayerState.playerRepeatMode,
            onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
            onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
            onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
            onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
            onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
            isShuffleMode = playerUiState.currentPlayerState.isShuffleMode,
            onShuffleModeClick = { onPlayerAction(PlayerActions.OnShuffleMode) },
        )
        VolumeController(
            maxDeviceVolume = maxDeviceVolume,
            currentVolume = { currentVolume },
            onVolumeChange = onVolumeChange,
        )
    }
}
