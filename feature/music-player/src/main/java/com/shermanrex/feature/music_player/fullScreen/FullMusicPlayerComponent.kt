package com.shermanrex.feature.music_player.fullScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.designsystem.util.DeviceSize
import com.shermanrex.core.designsystem.util.calculateWindowSize
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.PlayingMusicInfo
import com.shermanrex.core.model.PlayingMusicState
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.PlayerActions
import com.shermanrex.feature.music_player.fullScreen.component.LandscapeLayout
import com.shermanrex.feature.music_player.fullScreen.component.PortraitLayout
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FullMusicPlayerComponent(
    modifier: Modifier,
    playerUiState: PlayerUiState,
    pagerMusicList: ImmutableList<ArtworkModel>,
    onPlayerAction: (PlayerActions) -> Unit,
    onArtistClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val windowSize = calculateWindowSize()

    if (windowSize == DeviceSize.COMPACT) {
        PortraitLayout(
            modifier = modifier,
            playerUiState = playerUiState,
            pagerMusicList = pagerMusicList,
            setCurrentPagerIndex = { onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it)) },
            onVolumeChange = { onPlayerAction(PlayerActions.OnVolumeChange(it)) },
            onTimerClick = { onPlayerAction(PlayerActions.OnShowTimerBottomSheet) },
            seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
            onMoveToIndexPager = { index, musicId -> onPlayerAction(PlayerActions.OnMoveToMedia(index, musicId)) },
            onFavoriteClick = { onPlayerAction(PlayerActions.OnFavoriteToggle(it)) },
            onPreviousClick = { onPlayerAction(PlayerActions.OnPreviousMusic(false)) },
            onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
            onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
            onNextClick = { onPlayerAction(PlayerActions.OnNextMusic) },
            onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
            onShuffleModeClick = { onPlayerAction(PlayerActions.OnSetShuffleMode) },
            onArtistClick = onArtistClick,
            onBack = onBack,
        )
    } else {
        LandscapeLayout(
            modifier = modifier,
            playerUiState = playerUiState,
            pagerMusicList = pagerMusicList,
            setCurrentPagerIndex = { onPlayerAction(PlayerActions.UpdateArtworkPageIndex(it)) },
            onVolumeChange = { onPlayerAction(PlayerActions.OnVolumeChange(it)) },
            onTimerClick = { onPlayerAction(PlayerActions.OnShowTimerBottomSheet) },
            seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
            onMoveToIndexPager = { index, musicId -> onPlayerAction(PlayerActions.OnMoveToMedia(index, musicId)) },
            onFavoriteClick = { onPlayerAction(PlayerActions.OnFavoriteToggle(it)) },
            onPreviousClick = { onPlayerAction(PlayerActions.OnPreviousMusic(false)) },
            onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
            onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
            onNextClick = { onPlayerAction(PlayerActions.OnNextMusic) },
            onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
            onShuffleModeClick = { onPlayerAction(PlayerActions.OnSetShuffleMode) },
            onArtistClick = onArtistClick,
            onBack = onBack,
        )
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Preview(heightDp = 460, widthDp = 1000)
@Preview(heightDp = 460, widthDp = 600)
@Preview
@Composable
private fun FullScreenPreviewComponent() {
    MediaPlayerJetpackComposeTheme {
        FullMusicPlayerComponent(
            modifier = Modifier,
            playerUiState = PlayerUiState(
                currentPlayerState = PlayingMusicState(
                    playingMusicInfo = PlayingMusicInfo(
                        title = "Example Music name.mp3",
                        musicID = "1",
                        artworkUri = "",
                        musicUri = "",
                        artist = "Example Music artist",
                        duration = 20000L,
                        bitrate = 320_000,
                        size = 120_000,
                        album = "Example Music album",
                    ),
                    isPlaying = false,
                    isBuffering = false,
                    isFavorite = false,
                    playerRepeatMode = PlayerRepeatMode.MODE_OFF,
                    isShuffleMode = false,
                ),
                currentPlayerPosition = 1000L,
            ),
            pagerMusicList = emptyList<ArtworkModel>().toImmutableList(),
            onBack = {},
            onPlayerAction = {},
            onArtistClick = {},
        )
    }
}
