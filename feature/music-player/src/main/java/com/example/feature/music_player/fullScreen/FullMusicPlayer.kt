package com.example.feature.music_player.fullScreen

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowHeightSizeClass
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.ActiveMusicInfo
import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.fullScreen.component.LandscapeLayout
import com.example.feature.music_player.fullScreen.component.PortraitLayout
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FullMusicPlayer(
    modifier: Modifier,
    repeatMode: Int,
    currentPagerPage: Int,
    currentPlayerState: PlayerStateModel,
    currentMusicPosition: Long,
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
        playerStateModel = currentPlayerState,
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
    playerStateModel: PlayerStateModel,
    currentMusicPosition: Long,
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
        PortraitLayout(
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
    } else {
        LandscapeLayout(
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
}

@Preview(heightDp = 360, widthDp = 800)
@Preview(heightDp = 460, widthDp = 1000)
@Preview(heightDp = 460, widthDp = 600)
@Preview()
@PreviewScreenSizes
@Composable
private fun FullScreenPreview() {
    MediaPlayerJetpackComposeTheme {
        FullMusicPlayer(
            modifier = Modifier,
            isFavorite = false,
            currentPlayerState =
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
                ),
            repeatMode = 0,
            currentMusicPosition = 10000L,
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
