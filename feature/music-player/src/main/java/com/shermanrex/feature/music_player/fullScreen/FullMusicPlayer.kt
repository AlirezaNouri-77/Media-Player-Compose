package com.shermanrex.feature.music_player.fullScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.designsystem.util.CurrentWindowSizeState
import com.shermanrex.core.model.CurrentMusicInfo
import com.shermanrex.core.model.MusicPlayerState
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.WindowSize
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.PlayerActions
import com.shermanrex.feature.music_player.fullScreen.component.LandscapeLayout
import com.shermanrex.feature.music_player.fullScreen.component.PortraitLayout
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FullMusicPlayer(
    modifier: Modifier,
    playerRepeatMode: PlayerRepeatMode,
    currentPagerPage: Int,
    playerUiState: PlayerUiState,
    currentMusicPosition: Long,
    isFavorite: Boolean,
    pagerMusicList: ImmutableList<ArtworkModel>,
    onBack: () -> Unit,
    onPlayerAction: (PlayerActions) -> Unit,
    setCurrentPagerNumber: (Int) -> Unit,
    maxDeviceVolume: Int,
    currentVolume: Int,
    onVolumeChange: (Float) -> Unit,
    clickOnArtist: (String) -> Unit,
) {
    ExpandedMusicPlayer(
        modifier = modifier,
        playerRepeatMode = playerRepeatMode,
        currentPagerPage = currentPagerPage,
        playerUiState = playerUiState,
        currentMusicPosition = currentMusicPosition,
        isFavorite = isFavorite,
        pagerMusicList = pagerMusicList,
        onBack = onBack,
        onPlayerAction = onPlayerAction,
        setCurrentPagerNumber = setCurrentPagerNumber,
        maxDeviceVolume = maxDeviceVolume,
        currentVolume = currentVolume,
        onVolumeChange = onVolumeChange,
        clickOnArtist = clickOnArtist,
    )
}

@Composable
private fun ExpandedMusicPlayer(
    modifier: Modifier = Modifier,
    playerRepeatMode: PlayerRepeatMode,
    currentPagerPage: Int,
    playerUiState: PlayerUiState,
    currentMusicPosition: Long,
    isFavorite: Boolean,
    maxDeviceVolume: Int,
    currentVolume: Int,
    pagerMusicList: ImmutableList<ArtworkModel>,
    onBack: () -> Unit,
    onPlayerAction: (PlayerActions) -> Unit,
    setCurrentPagerNumber: (Int) -> Unit,
    onVolumeChange: (Float) -> Unit,
    clickOnArtist: (String) -> Unit,
) {
    val windowSize = CurrentWindowSizeState()

    if (windowSize == WindowSize.COMPACT) {
        PortraitLayout(
            modifier = modifier,
            playerRepeatMode = playerRepeatMode,
            currentPagerPage = currentPagerPage,
            playerUiState = playerUiState,
            currentMusicPosition = currentMusicPosition,
            isFavorite = isFavorite,
            pagerMusicList = pagerMusicList,
            onBack = onBack,
            onPlayerAction = onPlayerAction,
            setCurrentPagerNumber = setCurrentPagerNumber,
            maxDeviceVolume = maxDeviceVolume,
            currentVolume = currentVolume,
            onVolumeChange = onVolumeChange,
            clickOnArtist = clickOnArtist,
        )
    } else {
        LandscapeLayout(
            modifier = modifier,
            playerRepeatMode = playerRepeatMode,
            currentPagerPage = currentPagerPage,
            playerUiState = playerUiState,
            currentMusicPosition = currentMusicPosition,
            isFavorite = isFavorite,
            pagerMusicList = pagerMusicList,
            onBack = onBack,
            onPlayerAction = onPlayerAction,
            setCurrentPagerNumber = setCurrentPagerNumber,
            maxDeviceVolume = maxDeviceVolume,
            currentVolume = currentVolume,
            onVolumeChange = onVolumeChange,
            onArtistClick = clickOnArtist,
        )
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Preview(heightDp = 460, widthDp = 1000)
@Preview(heightDp = 460, widthDp = 600)
@Preview
@Composable
private fun FullScreenPreview() {
    MediaPlayerJetpackComposeTheme {
        FullMusicPlayer(
            modifier = Modifier,
            isFavorite = false,
            playerUiState = PlayerUiState(
                currentPlayerState = MusicPlayerState(
                    currentMusicInfo = CurrentMusicInfo(
                        title = "Example Music name.mp3",
                        musicID = "0",
                        artworkUri = "",
                        musicUri = "",
                        artist = "Example Music artist",
                        duration = 240_000,
                        bitrate = 320_000,
                        size = 120_000,
                        isFavorite = false,
                        album = "Example Music album",
                    ),
                    isPlaying = false,
                    isBuffering = false,
                    playerRepeatMode = PlayerRepeatMode.MODE_OFF,
                    isShuffleMode = false,
                ),
            ),
            playerRepeatMode = PlayerRepeatMode.MODE_OFF,
            currentMusicPosition = 10000L,
            currentPagerPage = 0,
            pagerMusicList = emptyList<ArtworkModel>().toImmutableList(),
            onBack = {},
            setCurrentPagerNumber = {},
            onPlayerAction = {},
            maxDeviceVolume = 10,
            currentVolume = 2,
            onVolumeChange = {},
            clickOnArtist = {},
        )
    }
}
