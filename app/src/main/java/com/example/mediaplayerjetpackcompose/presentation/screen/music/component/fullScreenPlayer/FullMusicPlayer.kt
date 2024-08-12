package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.PagerHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.HeaderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.PagerArtwork
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SliderSection
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component.SongDetail

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullMusicPlayer(
  favoriteList: SnapshotStateList<String>,
  currentMediaState: () -> MediaCurrentState,
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
    onMoveNextMusic = onMoveNextMusic,
    onMovePreviousMusic = onMovePreviousMusic,
  )

  val animateColor =
    animateColorAsState(
      targetValue = Color(backgroundColorByArtwork),
      animationSpec = tween(durationMillis = 500, delayMillis = 90),
      label = "",
    )
  val imageSize = remember {
    mutableStateOf(IntSize.Zero)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .consumeWindowInsets(WindowInsets(0)),
  )

  ConstraintLayout(
    modifier = Modifier
      .fillMaxSize()
      .onGloballyPositioned { imageSize.value = it.size }
      .background(
        Brush.verticalGradient(
          colors = listOf(animateColor.value.copy(alpha = 0.8f), animateColor.value.copy(alpha = 0.3f), Color.Black),
          startY = imageSize.value.height.toFloat() / 8,
          endY = imageSize.value.height.toFloat(),
        )
      )
      .pointerInput(Unit) {
        this.detectDragGestures { change, dragAmount ->
          change.changedToUpIgnoreConsumed()
          if (dragAmount.y > 80f) {
            onBack.invoke()
          }
        }
      }
  ) {
    val (headerRef, centerRef, controllerRef) = createRefs()
    HeaderSection(
      modifier = Modifier.constrainAs(headerRef) {
        top.linkTo(parent.top, margin = 30.dp)
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
      },
      onBackClick = {
        onBack.invoke()
      },
    )
    Column(
      modifier = Modifier.constrainAs(centerRef) {
        top.linkTo(parent.top)
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
        bottom.linkTo(parent.bottom)
      },
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
      PagerArtwork(
        modifier = Modifier
          .fillMaxWidth(),
        musicList = pagerMusicList,
        pagerState = pagerState,
      )
      SongDetail(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 25.dp),
        currentMediaCurrentState = { currentMediaState() },
      )
      SliderSection(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        currentMusicPosition = { currentMusicPosition() },
        seekTo = { onSeekTo.invoke(it) },
        duration = currentMediaState().metaData.extras?.getInt("Duration")?.toFloat() ?: 0f
      )
    }

    SongController(
      modifier = Modifier.constrainAs(controllerRef) {
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
        bottom.linkTo(parent.bottom)
        top.linkTo(centerRef.bottom)
      },
      currentMediaCurrentState = currentMediaState(),
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