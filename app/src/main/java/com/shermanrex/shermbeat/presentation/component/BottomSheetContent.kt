package com.shermanrex.shermbeat.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.util.MiniPlayerHeight
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.PlayerActions
import com.shermanrex.feature.music_player.PlayerViewModel
import com.shermanrex.feature.music_player.fullScreen.FullMusicPlayer
import com.shermanrex.feature.music_player.miniPlayer.MiniMusicPlayer
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    isVisible: Boolean,
    playerUiState: PlayerUiState,
    playerViewModel: PlayerViewModel,
    artworkDominateColor: Int,
    currentMusicPlayerPosition: Long,
    currentArtworkPagerIndex: Int,
    currentDeviceVolume: Int,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    pagerThumbnailList: ImmutableList<ArtworkModel>,
    bottomSheetSwapFraction: () -> Float,
    navigateToArtist: (String) -> Unit,
) {
    val musicArtWorkColorAnimation by animateColorAsState(
        targetValue = Color(artworkDominateColor),
        animationSpec = tween(durationMillis = 150, delayMillis = 90, easing = LinearEasing),
        label = "",
    )
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(200, delayMillis = 90)) + slideInVertically(
            animationSpec = tween(400, 250),
            initialOffsetY = { int -> int / 2 },
        ),
        exit = slideOutVertically(
            animationSpec = tween(400, 100),
            targetOffsetY = { int -> int / 2 },
        ) + fadeOut(tween(200, delayMillis = 90)),
    ) {
        Box {
            FullMusicPlayer(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = bottomSheetSwapFraction() }
                    .drawBehind {
                        drawRect(Color.Black)
                        drawRect(
                            Brush.verticalGradient(
                                0.4f to musicArtWorkColorAnimation.copy(alpha = 0.8f),
                                0.85f to musicArtWorkColorAnimation.copy(alpha = 0.3f),
                                1f to Color.Transparent,
                            ),
                        )
                        drawRect(
                            Brush.verticalGradient(
                                0.5f to Color.Black.copy(alpha = 0.5f),
                                1f to Color.Transparent,
                            ),
                        )
                    }
                    .navigationBarsPadding()
                    .padding(top = MiniPlayerHeight),
                isFavorite = playerUiState.currentPlayerState.currentMusicInfo.isFavorite,
                pagerMusicList = pagerThumbnailList.toImmutableList(),
                playerRepeatMode = playerUiState.currentPlayerState.playerRepeatMode,
                currentPagerPage = currentArtworkPagerIndex,
                onPlayerAction = playerViewModel::onPlayerAction,
                currentVolume = currentDeviceVolume,
                playerUiState = playerUiState,
                currentMusicPosition = currentMusicPlayerPosition,
                maxDeviceVolume = playerViewModel.getMaxDeviceVolume(),
                onVolumeChange = { playerViewModel.setDeviceVolume(it) },
                setCurrentPagerNumber = {
                    playerViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it))
                },
                onBack = {
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
                },
                clickOnArtist = { artistName ->
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.partialExpand() }
                    navigateToArtist(artistName)
                },
            )
            MiniMusicPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MiniPlayerHeight)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 8.dp)
                    .padding(top = 4.dp, bottom = 4.dp)
                    .alpha(1f - bottomSheetSwapFraction()),
                artworkPagerList = pagerThumbnailList.toImmutableList(),
                currentMusicPosition = currentMusicPlayerPosition,
                currentPagerPage = currentArtworkPagerIndex,
                onPlayerAction = playerViewModel::onPlayerAction,
                currentPlayerMediaId = playerUiState.currentPlayerState.currentMusicInfo.musicID.toLong(),
                currentPlayerDuration = playerUiState.currentPlayerState.currentMusicInfo.duration.toInt(),
                currentPlayerArtworkUri = playerUiState.currentPlayerState.currentMusicInfo.artworkUri,
                isPlaying = playerUiState.currentPlayerState.isPlaying,
                musicArtWorkColorAnimation = musicArtWorkColorAnimation,
                onClick = { coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() } },
                setCurrentPagerNumber = {
                    playerViewModel.onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it))
                },
            )
        }
    }

    if (playerUiState.shouldShowTimerBottomSheet) {
        TimerBottomSheet(
            onDismiss = { playerViewModel.onPlayerAction(PlayerActions.OnHideTimerBottomSheet) },
            selectedTimer = playerUiState.playerTimerState.playerTimerState,
            timerTimerLeft = playerUiState.playerTimerState.timerTimeLeft,
            onClick = {
                playerViewModel.onPlayerAction(PlayerActions.OnTimerClick(it))
            },
        )
    }
}
