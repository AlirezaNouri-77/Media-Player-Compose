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
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PortraitLayout(
    modifier: Modifier = Modifier,
    playerUiState: PlayerUiState,
    pagerMusicList: ImmutableList<ArtworkModel>,
    setCurrentPagerIndex: (Int) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onTimerClick: () -> Unit,
    onArtistClick: (String) -> Unit,
    seekTo: (Long) -> Unit,
    onMoveToIndexPager: (Int, String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onPreviousClick: () -> Unit,
    onPauseMusic: () -> Unit,
    onResumeMusic: () -> Unit,
    onNextClick: () -> Unit,
    onRepeatMode: (PlayerRepeatMode) -> Unit,
    onShuffleModeClick: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp,
            )
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBack,
        )
        FullscreenPlayerPager(
            modifier = Modifier.aspectRatio(1f),
            pagerItem = pagerMusicList,
            currentPagerPage = playerUiState.currentThumbnailPagerIndex,
            currentMusicID = playerUiState.currentPlayerState.playingMusicInfo.musicID.toLong(),
            setCurrentPagerIndex = setCurrentPagerIndex,
            onMoveToIndexPager = onMoveToIndexPager,
        )
        UtilityActionComponent(
            isFavorite = playerUiState.currentPlayerState.isFavorite,
            onTimerIconClick = onTimerClick,
            onFavoriteClick = { onFavoriteClick(playerUiState.currentPlayerState.playingMusicInfo.musicID) },
        )
        SongDetail(
            modifier = Modifier.padding(horizontal = 12.dp),
            playerUiState = playerUiState,
            onArtistClick = onArtistClick,
        )
        SliderSection(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            currentMusicPosition = playerUiState.currentPlayerPosition,
            duration = playerUiState.currentPlayerState.playingMusicInfo.duration.toFloat(),
            seekTo = seekTo,
        )
        SongController(
            isPlaying = playerUiState.currentPlayerState.isPlaying,
            playerRepeatMode = playerUiState.currentPlayerState.playerRepeatMode,
            isShuffleMode = playerUiState.currentPlayerState.isShuffleMode,
            onPauseMusic = onPauseMusic,
            onResumeMusic = onResumeMusic,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onRepeatMode = onRepeatMode,
            onShuffleModeClick = onShuffleModeClick,
        )
        VolumeController(
            maxDeviceVolume = playerUiState.maxDeviceVolume,
            currentVolume = playerUiState.currentDeviceVolume,
            onVolumeChange = onVolumeChange,
        )
    }
}
