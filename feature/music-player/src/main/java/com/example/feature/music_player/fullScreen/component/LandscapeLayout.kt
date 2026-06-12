package com.example.feature.music_player.fullScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.model.MusicPlayerState
import com.example.core.model.PlayerRepeatMode
import com.example.core.music_media3.model.ArtworkModel
import com.example.feature.music_player.PlayerActions
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun LandscapeLayout(
    modifier: Modifier = Modifier,
    playerRepeatMode: PlayerRepeatMode,
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
    onArtistClick: (String) -> Unit,
) {
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
                    .weight(1f, false)
                    .aspectRatio(0.9f),
                pagerItem = pagerMusicList,
                currentPagerPage = currentPagerPage,
                currentMusicID = musicPlayerState.currentMusicInfo.musicID.toLong(),
                onPlayerAction = { onPlayerAction(it) },
                setCurrentPagerNumber = { setCurrentPagerNumber(it) },
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
                currentMusicPlayerState = musicPlayerState,
                isFavorite = isFavorite,
                onArtistClick = onArtistClick,
                onFavoriteClick = {
                    onPlayerAction(PlayerActions.OnFavoriteToggle(musicPlayerState.currentMusicInfo.musicID))
                },
            )
            SliderSection(
                modifier = Modifier,
                currentMusicPosition = currentMusicPosition,
                seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
                duration = musicPlayerState.currentMusicInfo.duration.toFloat(),
            )
            SongController(
                isPlaying = musicPlayerState.isPlaying,
                playerRepeatMode = playerRepeatMode,
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
}
