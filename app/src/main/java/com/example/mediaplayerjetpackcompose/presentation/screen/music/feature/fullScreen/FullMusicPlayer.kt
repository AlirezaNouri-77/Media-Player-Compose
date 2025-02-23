package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.util.Constant
import com.example.mediaplayerjetpackcompose.core.model.MediaPlayerState
import com.example.mediaplayerjetpackcompose.core.model.MusicModel
import com.example.mediaplayerjetpackcompose.core.model.PlayerActions
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.FullscreenPlayerPager
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.HeaderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.SliderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.SongController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.SongDetail
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.VolumeController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.fullScreen.component.decoupledConstraintLayout
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FullMusicPlayer(
  modifier: Modifier,
  repeatMode: Int,
  currentPagerPage: Int,
  mediaPlayerState: () -> MediaPlayerState,
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

  ConstraintLayout(
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
      mediaPlayerState = mediaPlayerState(),
      onPlayerAction = { onPlayerAction(it) },
      setCurrentPagerNumber = { setCurrentPagerNumber(it) },
      currentPagerPage = currentPagerPage,
    )
    SongDetail(
      modifier = Modifier
        .layoutId("songDetail"),
      currentMediaPlayerState = { mediaPlayerState() },
    )
    SliderSection(
      modifier = Modifier
        .layoutId("slider"),
      currentMusicPosition = { currentMusicPosition() },
      seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
      duration = mediaPlayerState().metaData.extras?.getInt(Constant.DURATION_KEY)?.toFloat() ?: 0f
    )
    SongController(
      modifier = Modifier.layoutId("controllerRef"),
      mediaPlayerState = { mediaPlayerState() },
      isFavorite = isFavorite,
      repeatMode = repeatMode,
      onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
      onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
      onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
      onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
      onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
      onFavoriteToggle = { onPlayerAction(PlayerActions.OnFavoriteToggle(mediaPlayerState().mediaId)) }
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

@Preview(heightDp = 360, widthDp = 800)
@Preview(heightDp = 460, widthDp = 1000)
@Preview()
@Composable
private fun FullScreenPreview() {
  MediaPlayerJetpackComposeTheme {
    FullMusicPlayer(
      modifier = Modifier,
      isFavorite = false,
      mediaPlayerState = { MediaPlayerState.Empty },
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