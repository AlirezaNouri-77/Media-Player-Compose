package com.example.feature.music_player.fullScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.ActiveMusicInfo
import com.example.core.model.MusicModel
import com.example.core.music_media3.model.PlayerStateModel
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.fullScreen.component.FullscreenPlayerPager
import com.example.feature.music_player.fullScreen.component.HeaderSection
import com.example.feature.music_player.fullScreen.component.SliderSection
import com.example.feature.music_player.fullScreen.component.SongController
import com.example.feature.music_player.fullScreen.component.SongDetail
import com.example.feature.music_player.fullScreen.component.VolumeController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FullMusicPlayer(
    modifier: Modifier,
    repeatMode: Int,
    currentPagerPage: Int,
    playerStateModel: () -> PlayerStateModel,
    currentMusicPosition: () -> Long,
    isFavorite: Boolean,
    pagerMusicList: ImmutableList<MusicModel>,
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
        repeatMode = repeatMode,
        currentPagerPage = currentPagerPage,
        playerStateModel = playerStateModel,
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
    repeatMode: Int,
    currentPagerPage: Int,
    playerStateModel: () -> PlayerStateModel,
    currentMusicPosition: () -> Long,
    isFavorite: Boolean,
    pagerMusicList: ImmutableList<MusicModel>,
    onBack: () -> Unit,
    onPlayerAction: (PlayerActions) -> Unit,
    setCurrentPagerNumber: (Int) -> Unit,
    maxDeviceVolume: Int,
    currentVolume: Int,
    onVolumeChange: (Float) -> Unit,
    clickOnArtist: (String) -> Unit,
) {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass

    if (windowSize.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
        Column(
            modifier = modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderSection(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                onBackClick = onBack,
            )
            FullscreenPlayerPager(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                pagerItem = pagerMusicList,
                playerStateModel = playerStateModel(),
                onPlayerAction = { onPlayerAction(it) },
                setCurrentPagerNumber = { setCurrentPagerNumber(it) },
                currentPagerPage = currentPagerPage,
            )
            SongDetail(
                modifier = Modifier.padding(horizontal = 12.dp),
                currentPlayerStateModel = playerStateModel,
                clickOnArtist = clickOnArtist,
            )
            SliderSection(
                modifier = Modifier.padding(horizontal = 4.dp),
                currentMusicPosition = { currentMusicPosition() },
                seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
                duration = playerStateModel().currentMediaInfo.duration.toFloat(),
            )
            SongController(
                playerStateModel = { playerStateModel() },
                isFavorite = isFavorite,
                repeatMode = repeatMode,
                onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
                onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
                onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
                onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
                onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
                onFavoriteToggle = { onPlayerAction(PlayerActions.OnFavoriteToggle(playerStateModel().currentMediaInfo.musicID)) },
            )
            VolumeController(
                maxDeviceVolume = maxDeviceVolume,
                currentVolume = { currentVolume },
                onVolumeChange = onVolumeChange,
            )
        }
    } else {
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
                )
                FullscreenPlayerPager(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    pagerItem = pagerMusicList,
                    playerStateModel = playerStateModel(),
                    onPlayerAction = { onPlayerAction(it) },
                    setCurrentPagerNumber = { setCurrentPagerNumber(it) },
                    currentPagerPage = currentPagerPage,
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
                    currentPlayerStateModel = playerStateModel,
                    clickOnArtist = clickOnArtist,
                )
                SliderSection(
                    modifier = Modifier,
                    currentMusicPosition = { currentMusicPosition() },
                    seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
                    duration = playerStateModel().currentMediaInfo.duration.toFloat(),
                )
                SongController(
                    playerStateModel = { playerStateModel() },
                    isFavorite = isFavorite,
                    repeatMode = repeatMode,
                    onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
                    onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
                    onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
                    onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
                    onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
                    onFavoriteToggle = {
                        onPlayerAction(
                            PlayerActions.OnFavoriteToggle(playerStateModel().currentMediaInfo.musicID),
                        )
                    },
                )
                VolumeController(
                    maxDeviceVolume = maxDeviceVolume,
                    currentVolume = { currentVolume },
                    onVolumeChange = onVolumeChange,
                )
            }
        }
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Preview(heightDp = 460, widthDp = 1000)
@Preview(heightDp = 460, widthDp = 600)
@Preview()
@Composable
private fun FullScreenPreview() {
    MediaPlayerJetpackComposeTheme {
        FullMusicPlayer(
            modifier = Modifier,
            isFavorite = false,
            playerStateModel = {
                PlayerStateModel(
                    currentMediaInfo = ActiveMusicInfo(
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
                    repeatMode = 0,
                )
            },
            repeatMode = 0,
            currentMusicPosition = { 10000L },
            currentPagerPage = 0,
            pagerMusicList = persistentListOf(MusicModel.Dummy),
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
