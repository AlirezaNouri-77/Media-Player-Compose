package com.example.feature.music_player.fullScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.music_media3.PlayerStateModel
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.fullScreen.component.FullscreenPlayerPager
import com.example.feature.music_player.fullScreen.component.HeaderSection
import com.example.feature.music_player.fullScreen.component.SliderSection
import com.example.feature.music_player.fullScreen.component.SongController
import com.example.feature.music_player.fullScreen.component.SongDetail
import com.example.feature.music_player.fullScreen.component.VolumeController
import com.example.feature.music_player.fullScreen.component.decoupledConstraintLayout
import com.example.core.model.MusicModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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
  orientation: Int = LocalConfiguration.current.orientation,
  maxDeviceVolume: Int,
  currentVolume: Int,
  onVolumeChange: (Float) -> Unit,
) {

  androidx.constraintlayout.compose.ConstraintLayout(
    modifier = modifier,
    constraintSet = decoupledConstraintLayout(orientation)
  ) {

    HeaderSection(
      modifier = Modifier.layoutId("header"),
      onBackClick = {
        onBack.invoke()
      },
    )
    FullscreenPlayerPager(
      modifier = Modifier
        .layoutId("pagerArtwork"),
      pagerItem = pagerMusicList,
      playerStateModel = playerStateModel(),
      onPlayerAction = { onPlayerAction(it) },
      setCurrentPagerNumber = { setCurrentPagerNumber(it) },
      currentPagerPage = currentPagerPage,
    )
    SongDetail(
      modifier = Modifier
        .layoutId("songDetail"),
      currentPlayerStateModel = { playerStateModel() },
    )
    SliderSection(
      modifier = Modifier
        .layoutId("slider"),
      currentMusicPosition = { currentMusicPosition() },
      seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
      duration = playerStateModel().currentMediaInfo.duration.toFloat(),
    )
    SongController(
      modifier = Modifier.layoutId("controllerRef"),
      playerStateModel = { playerStateModel() },
      isFavorite = isFavorite,
      repeatMode = repeatMode,
      onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
      onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
      onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
      onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
      onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
      onFavoriteToggle = { onPlayerAction(PlayerActions.OnFavoriteToggle(playerStateModel().currentMediaInfo.musicID)) }
    )
    VolumeController(
      modifier = Modifier.layoutId("volumeSlider"),
      maxDeviceVolume = maxDeviceVolume,
      currentVolume = { currentVolume },
      onVolumeChange = {
        onVolumeChange(it)
      }
    )
  }
}

@androidx.compose.ui.tooling.preview.Preview(heightDp = 360, widthDp = 800)
@androidx.compose.ui.tooling.preview.Preview(heightDp = 460, widthDp = 1000)
@androidx.compose.ui.tooling.preview.Preview()
@Composable
private fun FullScreenPreview() {
  MediaPlayerJetpackComposeTheme {
    FullMusicPlayer(
      modifier = Modifier,
      isFavorite = false,
      playerStateModel = { PlayerStateModel.Companion.Empty },
      repeatMode = 0,
      currentMusicPosition = { 10000L },
      currentPagerPage = 0,
      pagerMusicList = emptyList<MusicModel>().toImmutableList(),
      onBack = {},
      setCurrentPagerNumber = {},
      onPlayerAction = {},
      maxDeviceVolume = 10,
      currentVolume = 2,
      onVolumeChange = {},
    )
  }
}