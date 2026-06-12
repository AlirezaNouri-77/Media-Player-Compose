package com.example.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.model.MusicPlayerState
import com.example.core.music_media3.model.ArtworkModel
import com.example.feature.music_player.PlayerActions
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PortraitLayout(
    modifier: Modifier = Modifier,
    repeatMode: Int,
    currentPagerPage: Int,
    musicPlayerState: MusicPlayerState,
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
    Column(
        modifier = modifier.padding(bottom = 32.dp).padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderSection(
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBack,
        )
        FullscreenPlayerPager(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            pagerItem = pagerMusicList,
            currentPagerPage = currentPagerPage,
            currentMusicID = musicPlayerState.currentMusicInfo.musicID.toLong(),
            onPlayerAction = { onPlayerAction(it) },
            setCurrentPagerNumber = { setCurrentPagerNumber(it) },
        )
        SongDetail(
            modifier = Modifier.padding(horizontal = 12.dp),
            onArtistClick = clickOnArtist,
            isFavorite = isFavorite,
            currentMusicPlayerState = musicPlayerState,
            onFavoriteClick = {
                onPlayerAction(PlayerActions.OnFavoriteToggle(musicPlayerState.currentMusicInfo.musicID))
            },
        )
        SliderSection(
            modifier = Modifier.padding(horizontal = 12.dp),
            currentMusicPosition = currentMusicPosition,
            seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
            duration = musicPlayerState.currentMusicInfo.duration.toFloat(),
        )
        SongController(
            isPlaying = musicPlayerState.isPlaying,
            repeatMode = repeatMode,
            onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
            onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
            onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
            onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
            onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
            isShuffleMode = musicPlayerState.isShuffleMode,
            onShuffleModeClick = { onPlayerAction(PlayerActions.onShuffleMode) },
        )
        VolumeController(
            maxDeviceVolume = maxDeviceVolume,
            currentVolume = { currentVolume },
            onVolumeChange = onVolumeChange,
        )
    }
}
