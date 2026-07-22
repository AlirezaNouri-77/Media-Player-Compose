package com.shermanrex.shermbeat.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.util.DeviceSize
import com.shermanrex.core.designsystem.util.calculateHeightWindowSize
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.feature.music_player.fullScreen.component.FullscreenPlayerPager
import com.shermanrex.feature.music_player.fullScreen.component.SliderSection
import com.shermanrex.feature.music_player.fullScreen.component.SongController
import com.shermanrex.feature.music_player.fullScreen.component.SongDetail
import com.shermanrex.feature.music_player.fullScreen.component.UtilityActionComponent
import com.shermanrex.feature.music_player.fullScreen.component.VolumeController
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun LandscapePlayerComponent(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    playerUiState: PlayerUiState,
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
) {
    val musicArtWorkColorAnimation by animateColorAsState(
        targetValue = Color(playerUiState.thumbnailDominantColor),
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 90,
            easing = LinearEasing,
        ),
        label = "",
    )
    AnimatedVisibility(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .clip(RoundedCornerShape(16.dp))
            .drawBehind {
                drawRect(Color.Black)
                drawRect(
                    Brush.verticalGradient(
                        0.2f to musicArtWorkColorAnimation.copy(alpha = 0.8f),
                        0.6f to musicArtWorkColorAnimation.copy(alpha = 0.3f),
                        1f to Color.Transparent,
                    ),
                )
                drawRect(
                    Brush.verticalGradient(
                        0.5f to Color.Black.copy(alpha = 0.5f),
                        1f to Color.Transparent,
                    ),
                )
            },
        visible = isVisible,
        enter = fadeIn(),
    ) {
        val isCompact = calculateHeightWindowSize() != DeviceSize.COMPACT
        if (isCompact) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FullscreenPlayerPager(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(vertical = 12.dp),
                    pagerItem = playerUiState.thumbnailsList.toImmutableList(),
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
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FullscreenPlayerPager(
                        modifier = Modifier.size(180.dp),
                        pagerItem = playerUiState.thumbnailsList.toImmutableList(),
                        currentPagerPage = playerUiState.currentThumbnailPagerIndex,
                        currentMusicID = playerUiState.currentPlayerState.playingMusicInfo.musicID.toLong(),
                        setCurrentPagerIndex = setCurrentPagerIndex,
                        onMoveToIndexPager = onMoveToIndexPager,
                    )
                    SongDetail(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(horizontal = 12.dp),
                        playerUiState = playerUiState,
                        onArtistClick = onArtistClick,
                    )
                }
                SliderSection(
                    currentMusicPosition = playerUiState.currentPlayerPosition,
                    seekTo = seekTo,
                    duration = playerUiState.currentPlayerState.playingMusicInfo.duration.toFloat(),
                )
                SongController(
                    isPlaying = playerUiState.currentPlayerState.isPlaying,
                    playerRepeatMode = playerUiState.currentPlayerState.playerRepeatMode,
                    onPauseMusic = onPauseMusic,
                    onResumeMusic = onResumeMusic,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onRepeatMode = onRepeatMode,
                    onShuffleModeClick = onShuffleModeClick,
                    isShuffleMode = playerUiState.currentPlayerState.isShuffleMode,
                )
                VolumeController(
                    maxDeviceVolume = playerUiState.maxDeviceVolume,
                    currentVolume = playerUiState.currentDeviceVolume,
                    onVolumeChange = onVolumeChange,
                )
            }
        }
    }
}
