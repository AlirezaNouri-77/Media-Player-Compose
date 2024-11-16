package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.data.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.PagerThumbnailModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.FullscreenPlayerPager
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.HeaderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SliderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongDetail
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.decoupledConstraintLayout
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun FullMusicPlayer(
  backgroundColorByArtwork: Int,
  repeatMode: Int,
  currentPagerPage: Int,
  currentMediaState: () -> CurrentMediaState,
  currentMusicPosition: () -> Long,
  favoriteList: List<String>,
  pagerMusicList: SnapshotStateList<PagerThumbnailModel>,
  onBack: () -> Unit,
  onPlayerAction: (action: PlayerActions) -> Unit,
  setCurrentPagerNumber: (Int) -> Unit,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  val pagerState = rememberPagerState(
    initialPage = currentPagerPage,
    pageCount = { pagerMusicList.size },
  )

  PagerHandler(
    currentMediaState = currentMediaState,
    pagerMusicList = pagerMusicList,
    currentPagerPage = currentPagerPage,
    pagerState = pagerState,
    setCurrentPagerNumber = setCurrentPagerNumber,
    onMoveToIndex = { onPlayerAction(PlayerActions.OnMoveToIndex(it)) },
  )

  val animateColor = animateColorAsState(
    targetValue = Color(backgroundColorByArtwork),
    animationSpec = tween(durationMillis = 250, delayMillis = 80),
    label = "",
  )

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize()
      .drawWithCache {
        onDrawBehind {
          drawRect(Color.Black)
          drawRect(
            Brush.verticalGradient(
              0.4f to animateColor.value.copy(alpha = 0.8f),
              0.7f to animateColor.value.copy(alpha = 0.3f),
              1f to Color.Black,
            )
          )
        }
      }
      .pointerInput(Unit) {
        this.detectDragGestures { change, dragAmount ->
          change.changedToUpIgnoreConsumed()
          if (dragAmount.y > 80f) {
            onBack.invoke()
          }
        }
      }
      .displayCutoutPadding()
      .navigationBarsPadding(),
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
      pagerState = pagerState,
    )
    SongDetail(
      modifier = Modifier
        .layoutId("songDetail"),
      currentCurrentMediaState = { currentMediaState() },
    )
    SliderSection(
      modifier = Modifier
        .layoutId("slider"),
      currentMusicPosition = { currentMusicPosition() },
      seekTo = { onPlayerAction(PlayerActions.SeekTo(it)) },
      duration = currentMediaState().metaData.extras?.getInt(Constant.DURATION_KEY)?.toFloat() ?: 0f
    )
    SongController(
      modifier = Modifier.layoutId("controllerRef"),
      currentMediaState = currentMediaState(),
      favoriteList = favoriteList,
      repeatMode = repeatMode,
      onPauseMusic = { onPlayerAction(PlayerActions.PausePlayer) },
      onResumeMusic = { onPlayerAction(PlayerActions.ResumePlayer) },
      onMovePreviousMusic = { onPlayerAction(PlayerActions.MovePreviousPlayer(false)) },
      onMoveNextMusic = { onPlayerAction(PlayerActions.MoveNextPlayer) },
      onRepeatMode = { onPlayerAction(PlayerActions.OnRepeatMode(it)) },
      onFavoriteToggle = { onPlayerAction(PlayerActions.OnFavoriteToggle(currentMediaState().mediaId)) }
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
      favoriteList = emptyList<String>().toMutableStateList(),
      currentMediaState = { CurrentMediaState.Empty },
      backgroundColorByArtwork = MediaThumbnailUtil.DefaultColorPalette,
      repeatMode = 0,
      currentMusicPosition = { 10000L },
      currentPagerPage = 0,
      pagerMusicList = emptyList<PagerThumbnailModel>().toMutableStateList(),
      onBack = { /*TODO*/ },
      setCurrentPagerNumber = {},
      onPlayerAction = {},
    )
  }
}