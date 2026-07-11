package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
internal fun LandscapeLayout(
    modifier: Modifier = Modifier,
    playerUiState: PlayerUiState,
    pagerMusicList: ImmutableList<ArtworkModel>,
    onBack: () -> Unit,
    onPlayerAction: (PlayerActions) -> Unit,
    setCurrentPagerNumber: (Int) -> Unit,
    maxDeviceVolume: Int,
    currentVolume: Int,
    onVolumeChange: (Float) -> Unit,
    onArtistClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.displayCutoutPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            HeaderSection(
                modifier = Modifier.padding(start = 6.dp),
                onBackClick = onBack,
                onTimerClick = {
                    onPlayerAction(PlayerActions.OnShowTimerBottomSheet)
                },
            )
            FullscreenPlayerPager(
                modifier = Modifier
                    .weight(1f, false)
                    .aspectRatio(0.9f),
                pagerItem = pagerMusicList,
                currentPagerPage = playerUiState.currentThumbnailPagerIndex,
                currentMusicID = playerUiState.currentPlayerState.playingMusicInfo.musicID.toLong(),
                onPlayerAction = { onPlayerAction(it) },
                setCurrentPagerNumber = { setCurrentPagerNumber(it) },
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SongDetail(
                modifier = Modifier,
                playerUiState = playerUiState,
                isFavorite = playerUiState.currentPlayerState.isFavorite,
                onArtistClick = onArtistClick,
                onFavoriteClick = {
                    onPlayerAction(PlayerActions.OnFavoriteToggle(playerUiState.currentPlayerState.playingMusicInfo.musicID))
                },
            )
            SliderSection(
                modifier = Modifier,
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
}
