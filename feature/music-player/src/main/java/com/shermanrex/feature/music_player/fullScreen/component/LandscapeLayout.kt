package com.shermanrex.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.shermanrex.core.model.PlayerRepeatMode
import com.shermanrex.core.model.PlayingMusicInfo
import com.shermanrex.core.model.PlayingMusicState
import com.shermanrex.core.music_media3.model.ArtworkModel
import com.shermanrex.feature.music_player.model.PlayerUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun LandscapeLayout(
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
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        FullscreenPlayerPager(
            modifier = Modifier
                .weight(1f, false)
                .aspectRatio(1f)
                .fillMaxWidth()
                .displayCutoutPadding()
                .weight(0.4f),
            pagerItem = pagerMusicList,
            currentPagerPage = playerUiState.currentThumbnailPagerIndex,
            currentMusicID = playerUiState.currentPlayerState.playingMusicInfo.musicID.toLong(),
            setCurrentPagerIndex = setCurrentPagerIndex,
            onMoveToIndexPager = onMoveToIndexPager,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderSection(
                modifier = Modifier.padding(start = 6.dp),
                onBackClick = onBack,
            )
            SongDetail(
                playerUiState = playerUiState,
                onArtistClick = onArtistClick,
            )
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

@Preview(heightDp = 360, widthDp = 800)
@Preview(heightDp = 460, widthDp = 1000)
@Preview(heightDp = 460, widthDp = 600)
@Composable
private fun LandscapeLayoutPreview() {
    MediaPlayerJetpackComposeTheme {
        LandscapeLayout(
            playerUiState = PlayerUiState(
                currentPlayerState = PlayingMusicState(
                    playingMusicInfo = PlayingMusicInfo(
                        title = "Example Music name.mp3",
                        musicID = "0",
                        artworkUri = "",
                        musicUri = "",
                        artist = "Example Music artist",
                        duration = 240_000,
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
            ),
            pagerMusicList = emptyList<ArtworkModel>().toImmutableList(),
            onBack = {},
            setCurrentPagerIndex = {},
            onVolumeChange = {},
            onTimerClick = {},
            onArtistClick = {},
            seekTo = {},
            onMoveToIndexPager = { _, _ -> },
            onFavoriteClick = {},
            onPreviousClick = {},
            onPauseMusic = {},
            onResumeMusic = {},
            onNextClick = {},
            onRepeatMode = {},
            onShuffleModeClick = {},
        )
    }
}
