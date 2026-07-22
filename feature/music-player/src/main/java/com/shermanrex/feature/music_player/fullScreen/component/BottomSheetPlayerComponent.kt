package com.shermanrex.feature.music_player.fullScreen.component

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
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
fun BottomSheetPlayerComponent(
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
            .fillMaxSize()
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBack,
        )
        FullscreenPlayerPager(
            modifier = Modifier.weight(0.3f).padding(vertical = 12.dp),
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
        Spacer(Modifier.height(16.dp))
        VolumeController(
            maxDeviceVolume = playerUiState.maxDeviceVolume,
            currentVolume = playerUiState.currentDeviceVolume,
            onVolumeChange = onVolumeChange,
        )
    }
}

@Preview
@Composable
private fun BottomSheetPlayerComponentPreview() {
    MediaPlayerJetpackComposeTheme {
        BottomSheetPlayerComponent(
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
                maxDeviceVolume = 10,
            ),
            pagerMusicList = listOf<ArtworkModel>(
                ArtworkModel(
                    musicId = "1",
                    uri = Uri.EMPTY,
                    name = "test 1",
                    artist = "test 1"
                ),ArtworkModel(
                    musicId = "2",
                    uri = Uri.EMPTY,
                    name = "test 1",
                    artist = "test 1"
                ),ArtworkModel(
                    musicId = "2",
                    uri = Uri.EMPTY,
                    name = "test 1",
                    artist = "test 1"
                )
            ).toImmutableList(),
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
