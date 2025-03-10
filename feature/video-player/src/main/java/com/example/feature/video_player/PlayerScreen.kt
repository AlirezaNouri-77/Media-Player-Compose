package com.example.feature.video_player

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.example.feature.video.VideoPageViewModel
import com.example.feature.video_player.component.MiddleInfoHandler
import com.example.feature.video_player.component.PlayerControllerLayout
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(UnstableApi::class)
@kotlin.OptIn(FlowPreview::class)
@Composable
fun VideoPlayer(
  videoUri: String,
  videoPageViewModel: VideoPageViewModel,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  orientation: Int = LocalConfiguration.current.orientation,
  onBack: () -> Unit = {},
) {

  val currentPlayerPosition by videoPageViewModel.currentPlayerPosition.collectAsStateWithLifecycle(initialValue = 0)
  val currentState by videoPageViewModel.playerStateModel.collectAsStateWithLifecycle()
  val previewSliderBitmap by videoPageViewModel.previewSliderBitmap.collectAsStateWithLifecycle(null)

  val currentDeviceOrientation by remember(orientation) {
    mutableIntStateOf(orientation)
  }
  var middleVideoPlayerIndicator by remember {
    mutableStateOf<MiddleVideoPlayerIndicator>(MiddleVideoPlayerIndicator.Initial)
  }
  var controllerLayoutPadding by remember {
    mutableStateOf(PaddingValues(start = 10.dp, end = 10.dp))
  }
  var sliderValuePosition by remember {
    mutableFloatStateOf(0f)
  }
  var showInfoMiddleScreen by remember {
    mutableStateOf(false)
  }
  var playerControllerVisibility by remember {
    mutableStateOf(false)
  }

  BackHandler {
    onBack()
  }
  LaunchedEffect(key1 = showInfoMiddleScreen, key2 = sliderValuePosition) {
    delay(2500L)
    showInfoMiddleScreen = false
  }

  LaunchedEffect(key1 = sliderValuePosition) {
    snapshotFlow {
      sliderValuePosition
    }.debounce(100L)
      .distinctUntilChanged()
      .collectLatest {
        videoPageViewModel.getSliderPreviewThumbnail(sliderValuePosition.toLong())
      }
  }

  LaunchedEffect(
    key1 = orientation,
    block = {
      when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
          controllerLayoutPadding = PaddingValues(start = 25.dp, end = 25.dp)
          videoPageViewModel.playerResizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
        }

        Configuration.ORIENTATION_PORTRAIT -> {
          controllerLayoutPadding = PaddingValues(start = 10.dp, end = 10.dp)
          videoPageViewModel.playerResizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
      }
    },
  )

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

  AndroidView(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .pointerInput(null) {
        detectTapGestures(
          onTap = {
            playerControllerVisibility = !playerControllerVisibility
          },
          onDoubleTap = { offset ->
            when {
              offset.x > this.size.width / 2 -> {
                videoPageViewModel.fastForward(15_000, currentPlayerPosition)
                middleVideoPlayerIndicator = MiddleVideoPlayerIndicator.FastSeek(FastSeekMode.FastForward)
                showInfoMiddleScreen = true
              }

              offset.x < this.size.width / 2 -> {
                videoPageViewModel.fastRewind(15_000, currentPlayerPosition)
                middleVideoPlayerIndicator = MiddleVideoPlayerIndicator.FastSeek(FastSeekMode.FastRewind)
                showInfoMiddleScreen = true
              }
            }
          }
        )
      },
    factory = {
      PlayerView(it).apply {
        player = videoPageViewModel.getExoPlayer()
        useController = false
        resizeMode = videoPageViewModel.playerResizeMode
      }
    },
    update = {
      it.resizeMode = videoPageViewModel.playerResizeMode
    }
  )

  PlayerControllerLayout(
    isVisible = playerControllerVisibility,
    controllerLayoutPadding = controllerLayoutPadding,
    currentDeviceOrientation = currentDeviceOrientation,
    playerResizeMode = videoPageViewModel.playerResizeMode,
    previewSlider = previewSliderBitmap,
    onBackClick = {
      onBack()
      videoPageViewModel.stopPlayer()
    },
    currentPlayerState = { currentState },
    currentPlayerPosition = { currentPlayerPosition },
    slideSeekPosition = { sliderValuePosition },
    slidePositionChange = { sliderValuePosition = it },
    playerResizeModeChange = { videoPageViewModel.playerResizeMode = it },
    onMiddleVideoPlayerIndicator = { middleVideoPlayerIndicator = it },
    onSeekToPrevious = videoPageViewModel::seekToPrevious,
    onSeekToNext = videoPageViewModel::seekToNext,
    onPausePlayer = videoPageViewModel::pausePlayer,
    onResumePlayer = videoPageViewModel::resumePlayer,
    onSeekToPosition = videoPageViewModel::seekToPosition,
  )

  MiddleInfoHandler(
    modifier = Modifier,
    showInfoMiddleScreen = showInfoMiddleScreen,
    seekPosition = sliderValuePosition.toLong(),
    middleVideoPlayerIndicator = middleVideoPlayerIndicator,
  )

}