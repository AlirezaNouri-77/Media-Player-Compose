package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.mediaplayerjetpackcompose.data.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.domain.model.share.PlayerActions
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.component.verticalFadeEdge
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.HeaderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.PagerArtwork
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SliderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongDetail
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.decoupledConstraintLayout
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun FullMusicPlayer(
  favoriteList: SnapshotStateList<String>,
  currentMediaState: () -> CurrentMediaState,
  backgroundColorByArtwork: Int,
  repeatMode: Int,
  currentMusicPosition: () -> Long,
  currentPagerPage: Int,
  pagerMusicList: List<MusicModel>,
  onBack: () -> Unit,
  onPlayerAction: (action: PlayerActions) -> Unit,
  setCurrentPagerNumber: (Int) -> Unit,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  val pagerState = rememberPagerState(
    initialPage = currentPagerPage,
    pageCount = { pagerMusicList.size },
  )

  val titlePadding = remember(orientation) {
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) 10.dp else 20.dp
  }

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
      },
    constraintSet = decoupledConstraintLayout(orientation)
  ) {

    HeaderSection(
      modifier = Modifier.layoutId("header"),
      onBackClick = {
        onBack.invoke()
      },
    )
    PagerArtwork(
      modifier = Modifier
        .layoutId("pagerArtwork"),
      musicList = pagerMusicList,
      pagerState = pagerState,
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .layoutId("titleRef")
        .verticalFadeEdge()
        .basicMarquee()
        .padding(horizontal = titlePadding),
      text = currentMediaState().metaData.title?.removeFileExtension() ?: "Nothing Play",
      fontSize = 20.sp,
      fontWeight = FontWeight.SemiBold,
      overflow = TextOverflow.Visible,
      color = Color.White,
      maxLines = 1,
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
      currentCurrentMediaState = currentMediaState(),
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
      pagerMusicList = listOf(MusicModel.Empty),
      onBack = { /*TODO*/ },
      setCurrentPagerNumber = {},
      onPlayerAction = {},
    )
  }
}