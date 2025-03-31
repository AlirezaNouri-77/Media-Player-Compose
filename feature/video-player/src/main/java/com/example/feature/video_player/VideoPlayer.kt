package com.example.feature.video_player

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import com.example.feature.video.VideoPageViewModel
import com.example.feature.video.model.MiddleVideoPlayerIndicator
import com.example.feature.video_player.component.MiddleInfoHandler
import com.example.feature.video_player.component.PlayerControllerLayout
import com.example.feature.video_player.util.HideSystemWindowInset

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
  videoUri: String,
  videoPageViewModel: VideoPageViewModel,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  onBack: () -> Unit = {},
) {

  val currentPlayerPosition by videoPageViewModel.currentPlayerPosition.collectAsStateWithLifecycle(initialValue = 0)
  val currentState by videoPageViewModel.currentPlayerState.collectAsStateWithLifecycle()
  val previewSliderBitmap by videoPageViewModel.previewSliderBitmap.collectAsStateWithLifecycle(null)
  val middleVideoPlayerInfo by videoPageViewModel.middleVideoPlayerInfo.collectAsStateWithLifecycle(MiddleVideoPlayerIndicator.Initial)

  var playerControllerVisibility by remember {
    mutableStateOf(false)
  }

  BackHandler {
    onBack()
  }

  HideSystemWindowInset()

  DisposableEffect(key1 = lifecycleOwner, effect = {
    val observe = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_START -> videoPageViewModel.resumePlayer()
        Lifecycle.Event.ON_STOP -> videoPageViewModel.pausePlayer()
        else -> {}
      }
    }
    lifecycleOwner.lifecycle.addObserver(observe)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observe)
    }
  })

  BackHandler(enabled = true) {
    videoPageViewModel.stopPlayer()
    onBack.invoke()
  }

  LaunchedEffect(
    key1 = videoUri,
    block = {
      if (videoUri.isNotEmpty()) {
        videoPageViewModel.startPlayFromUri(videoUri.toUri())
      }
    },
  )

  val playerContentScale = remember { mutableStateOf(ContentScale.Fit) }
  val presentationState = rememberPresentationState(videoPageViewModel.getExoPlayer)
  val scaledModifier = Modifier.resizeWithContentScale(playerContentScale.value, presentationState.videoSizeDp)

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .playerViewClickHandler(
        onTapClick = { playerControllerVisibility = !playerControllerVisibility },
        onRightDoubleClick = {
          videoPageViewModel.fastForward(15_000, currentPlayerPosition)
          videoPageViewModel.updateMiddleVideoPlayerInfo(MiddleVideoPlayerIndicator.FastForward())
        },
        onLeftDoubleClick = {
          videoPageViewModel.fastRewind(15_000, currentPlayerPosition)
          videoPageViewModel.updateMiddleVideoPlayerInfo(MiddleVideoPlayerIndicator.FastRewind())
        }
      )
  ) {
    PlayerSurface(
      player = videoPageViewModel.getExoPlayer,
      modifier = scaledModifier,
      surfaceType = SURFACE_TYPE_SURFACE_VIEW,
    )

    if (presentationState.coverSurface) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black)
      )
    }
  }

  AnimatedVisibility(
    visible = playerControllerVisibility,
  ) {
    PlayerControllerLayout(
      modifier = Modifier
        .systemBarsPadding()
        .statusBarsPadding(),
      playerResizeMode = { playerContentScale.value },
      previewSlider = { previewSliderBitmap },
      currentPlayerState = { currentState },
      currentPlayerPosition = { currentPlayerPosition },
      getPreviewSlider = { videoPageViewModel.getSliderPreviewThumbnail(it.toLong()) },
      onBackClick = {
        onBack()
        videoPageViewModel.stopPlayer()
      },
      playerResizeModeChange = {
        playerContentScale.value = if (playerContentScale.value == ContentScale.Fit) ContentScale.FillWidth else ContentScale.Fit
      },
      onSeekToPrevious = videoPageViewModel::seekToPrevious,
      onSeekToNext = videoPageViewModel::seekToNext,
      onPausePlayer = videoPageViewModel::pausePlayer,
      onResumePlayer = videoPageViewModel::resumePlayer,
      onSeekToPosition = videoPageViewModel::seekToPosition,
    )
  }

  // show a info when fastforwar or fastrewind triggered
  MiddleInfoHandler(
    modifier = Modifier,
    showInfoMiddleScreen = middleVideoPlayerInfo != MiddleVideoPlayerIndicator.Initial,
    middleVideoPlayerIndicator = middleVideoPlayerInfo,
  )

}


private fun Modifier.playerViewClickHandler(
  onTapClick: () -> Unit,
  onRightDoubleClick: () -> Unit,
  onLeftDoubleClick: () -> Unit,
): Modifier = this.then(
  Modifier.pointerInput(null) {
    detectTapGestures(
      onTap = {
        onTapClick()
      },
      onDoubleTap = { offset ->
        when {
          offset.x > this.size.width / 2 -> onRightDoubleClick()
          offset.x < this.size.width / 2 -> onLeftDoubleClick()
        }
      }
    )
  }
)