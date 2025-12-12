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
import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel
import com.example.feature.music_player.PlayerActions
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PortraitLayout(
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
    Column(
        modifier = modifier.padding(bottom = 12.dp),
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
            playerStateModel = playerStateModel,
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
            modifier = Modifier.padding(horizontal = 12.dp),
            currentMusicPosition = currentMusicPosition,
            seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
            duration = playerStateModel.currentMediaInfo.duration.toFloat(),
        )
        SongController(
            isPlaying = playerStateModel.isPlaying,
            isFavorite = isFavorite,
            repeatMode = repeatMode,
            onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
            onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
            onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
            onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
            onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
            onFavoriteToggle = { onPlayerAction(PlayerActions.OnFavoriteToggle(playerStateModel.currentMediaInfo.musicID)) },
        )
        VolumeController(
            maxDeviceVolume = maxDeviceVolume,
            currentVolume = { currentVolume },
            onVolumeChange = onVolumeChange,
        )
    }
}
