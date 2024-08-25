package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
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
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.component.verticalFadeEdge
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.HeaderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.PagerArtwork
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SliderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongDetail
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullMusicPlayer(
  favoriteList: SnapshotStateList<String>,
  currentMediaState: () -> CurrentMediaState,
  backgroundColorByArtwork: Long,
  repeatMode: Int,
  currentMusicPosition: () -> Long,
  currentPagerPage: Int,
  pagerMusicList: List<MusicModel>,
  onBack: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: (Boolean) -> Unit,
  setCurrentPagerNumber: (Int) -> Unit,
  onSeekTo: (Long) -> Unit,
  onRepeatMode: (Int) -> Unit,
  onFavoriteToggle: () -> Unit,
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
    onMoveNextMusic = onMoveNextMusic,
    onMovePreviousMusic = onMovePreviousMusic,
  )

  val animateColor = animateColorAsState(
      targetValue = Color(backgroundColorByArtwork),
      animationSpec = tween(durationMillis = 250, delayMillis = 80),
      label = "",
    )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .consumeWindowInsets(WindowInsets(0)),
  )

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          0.4f to animateColor.value.copy(alpha = 0.8f),
          0.7f to animateColor.value.copy(alpha = 0.3f),
          1f to Color.Black,
        )
      )
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
      text = currentMediaState().metaData.title?.toString()?.removeFileExtension()
        ?: "Nothing Play",
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
      seekTo = { onSeekTo.invoke(it) },
      duration = currentMediaState().metaData.extras?.getInt("Duration")?.toFloat() ?: 0f
    )
    SongController(
      modifier = Modifier.layoutId("controllerRef"),
      currentCurrentMediaState = currentMediaState(),
      favoriteList = favoriteList,
      repeatMode = repeatMode,
      onPauseMusic = { onPauseMusic() },
      onResumeMusic = { onResumeMusic() },
      onMovePreviousMusic = { onMovePreviousMusic(false) },
      onMoveNextMusic = { onMoveNextMusic() },
      onRepeatMode = { onRepeatMode(it) },
      onFavoriteToggle = { onFavoriteToggle() }
    )

  }
}

private fun decoupledConstraintLayout(
  orientation:Int,
): ConstraintSet {
  return ConstraintSet {

    val headerRef = createRefFor("header")

    val songDetail = createRefFor("songDetail")
    val slider = createRefFor("slider")
    val titleRef = createRefFor("titleRef")
    val controllerRef = createRefFor("controllerRef")
    val pagerArtWork = createRefFor("pagerArtwork")

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {

      val bottomGuideline = createGuidelineFromBottom(70.dp)
      val topGuideline = createGuidelineFromTop(50.dp)
      val startGuideLine = createGuidelineFromStart(10.dp)
      val endGuideLine = createGuidelineFromEnd(10.dp)

      constrain(headerRef) {
        top.linkTo(topGuideline)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(headerRef.bottom, margin = 40.dp)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(songDetail.top, margin = 10.dp)
      }
      constrain(titleRef) {
        start.linkTo(parent.start, margin = 20.dp)
        end.linkTo(parent.end, margin = 20.dp)
        bottom.linkTo(songDetail.top, margin = 10.dp)
      }
      constrain(songDetail) {
        start.linkTo(parent.start, margin = 20.dp)
        end.linkTo(parent.end, margin = 20.dp)
        bottom.linkTo(slider.top, margin = 10.dp)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        start.linkTo(songDetail.start, margin = 10.dp)
        end.linkTo(songDetail.end, margin = 10.dp)
        bottom.linkTo(controllerRef.top, margin = 30.dp)
        width = Dimension.fillToConstraints
      }
      constrain(controllerRef) {
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
        bottom.linkTo(bottomGuideline)
      }

    } else {

      val bottomGuideline = createGuidelineFromBottom(20.dp)
      val topGuideline = createGuidelineFromTop(20.dp)
      val startGuideLine = createGuidelineFromStart(40.dp)
      val endGuideLine = createGuidelineFromEnd(40.dp)

      constrain(headerRef) {
        top.linkTo(topGuideline)
        start.linkTo(pagerArtWork.end)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(parent.top)
        start.linkTo(startGuideLine)
        bottom.linkTo(bottomGuideline)
      }
      constrain(titleRef) {
        top.linkTo(headerRef.bottom, margin = 30.dp)
        start.linkTo(pagerArtWork.end)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(songDetail) {
        top.linkTo(titleRef.bottom)
        start.linkTo(titleRef.start, margin = 10.dp)
        end.linkTo(titleRef.end)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        top.linkTo(songDetail.bottom, margin = 10.dp)
        start.linkTo(songDetail.start, margin = 10.dp)
        end.linkTo(songDetail.end, margin = 10.dp)
        width = Dimension.fillToConstraints
      }
      constrain(controllerRef) {
        top.linkTo(slider.bottom)
        start.linkTo(slider.start)
        end.linkTo(slider.end)
        bottom.linkTo(pagerArtWork.bottom)
      }

    }
  }
}

@Preview(heightDp = 360, widthDp = 800)
@Preview()
@Composable
private fun FullScreenPreview() {
  MediaPlayerJetpackComposeTheme {
    FullMusicPlayer(
      favoriteList = emptyList<String>().toMutableStateList(),
      currentMediaState = { CurrentMediaState.Empty },
      backgroundColorByArtwork = Long.MIN_VALUE,
      repeatMode = 0,
      currentMusicPosition = { 10000L },
      currentPagerPage = 0,
      pagerMusicList = listOf(MusicModel.Empty),
      onBack = { /*TODO*/ },
      onPauseMusic = { /*TODO*/ },
      onResumeMusic = { /*TODO*/ },
      onMoveNextMusic = { /*TODO*/ },
      onMovePreviousMusic = {},
      setCurrentPagerNumber = {},
      onSeekTo = {},
      onRepeatMode = {},
      onFavoriteToggle = {}
    )
  }
}