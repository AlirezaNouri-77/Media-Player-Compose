package com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.util.decodeStringNavigation
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.FastSeekMode
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.MiddleVideoPlayerIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component.MiddleInfoHandler
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component.PlayerController
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component.PlayerControllerButton
import com.example.mediaplayerjetpackcompose.presentation.screen.video.playerScreen.component.PlayerTimeLine
import kotlinx.coroutines.delay

@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
  videoUri: String,
  videoPageViewModel: VideoPageViewModel,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  orientation: Int = LocalConfiguration.current.orientation,
  onBackClick: () -> Unit = {},
) {

  val currentPlayerPosition by videoPageViewModel.currentMediaPosition.collectAsStateWithLifecycle(
    initialValue = 0
  )
  val currentState = videoPageViewModel.currentMediaState.collectAsStateWithLifecycle()

  val currentDeviceOrientation by remember(orientation) {
    mutableIntStateOf(orientation)
  }
  var middleVideoPlayerIndicator by remember {
    mutableStateOf<MiddleVideoPlayerIndicator>(MiddleVideoPlayerIndicator.Initial)
  }
  var controllerLayoutPadding by remember {
    mutableStateOf(PaddingValues(horizontal = 5.dp, vertical = 10.dp))
  }
  var sliderValuePosition by remember {
    mutableFloatStateOf(0f)
  }
  var isSliderInteraction by remember {
    mutableStateOf(false)
  }
  var showInfoMiddleScreen by remember {
    mutableStateOf(false)
  }

  var playerControllerVisibility by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = showInfoMiddleScreen, key2 = sliderValuePosition) {
    delay(1500L)
    showInfoMiddleScreen = false
  }

  LaunchedEffect(key1 = sliderValuePosition) {
    videoPageViewModel.getSliderPreviewThumbnail(sliderValuePosition.toLong())
  }

  LaunchedEffect(
    key1 = orientation,
    block = {
      when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
          controllerLayoutPadding = PaddingValues(horizontal = 15.dp, vertical = 12.dp)
          videoPageViewModel.playerResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }

        Configuration.ORIENTATION_PORTRAIT -> {
          controllerLayoutPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp)
          videoPageViewModel.playerResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
      }
    },
  )

  DisposableEffect(key1 = lifecycleOwner, effect = {
    val observe = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_DESTROY -> videoPageViewModel.releasePlayer()
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
    onBackClick.invoke()
  }

  LaunchedEffect(key1 = videoUri, block = {
    if (videoUri.isNotEmpty()) {
      videoPageViewModel.startPlayFromUri(videoUri.decodeStringNavigation())
    }
  })

  AndroidView(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(null) {
        detectTapGestures(
          onTap = {
            playerControllerVisibility = !playerControllerVisibility
          },
          onDoubleTap = { offset ->
            when {
              offset.x > this.size.width / 2 -> {
                videoPageViewModel.fastForward(15_000, currentPlayerPosition)
                middleVideoPlayerIndicator =
                  MiddleVideoPlayerIndicator.FastSeek(FastSeekMode.FastForward)
                showInfoMiddleScreen = true
              }

              offset.x < this.size.width / 2 -> {
                videoPageViewModel.fastRewind(15_000, currentPlayerPosition)
                middleVideoPlayerIndicator =
                  MiddleVideoPlayerIndicator.FastSeek(FastSeekMode.FastRewind)
                showInfoMiddleScreen = true
              }
            }
          }
        )
      }
      .background(Color.Transparent),
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

  MiddleInfoHandler(
    modifier = Modifier,
    showInfoMiddleScreen = showInfoMiddleScreen,
    seekPosition = sliderValuePosition.toLong(),
    middleVideoPlayerIndicator = middleVideoPlayerIndicator,
  )

  AnimatedVisibility(
    modifier = Modifier.fillMaxSize(),
    visible = playerControllerVisibility,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {

    ConstraintLayout(
      modifier = Modifier
        .fillMaxSize()
        .padding(controllerLayoutPadding)
        .then(
          if (currentDeviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Modifier
              .navigationBarsPadding()
              .displayCutoutPadding()
          } else Modifier
        ),
    ) {
      val (topSec, bottomSec, sliderThumbnailPreview) = createRefs()

      if (isSliderInteraction) {
        if (videoPageViewModel.previewSlider.value != null) {
          Image(
            bitmap = videoPageViewModel.previewSlider.value!!,
            contentDescription = "",
            modifier = Modifier
              .size(180.dp)
              .clip(RoundedCornerShape(10.dp))
              .constrainAs(sliderThumbnailPreview) {
                bottom.linkTo(bottomSec.top, margin = 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
              },
          )
        }
      }


      Row(
        modifier = Modifier
          .constrainAs(topSec) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .background(Color.Transparent)
          .fillMaxWidth()
          .drawBehind {
            drawRoundRect(
              color = Color.Black,
              size = this.size,
              alpha = 0.4f,
              cornerRadius = CornerRadius(25f, 25f),
            )
          },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Image(
          painter = painterResource(id = R.drawable.icon_back_24), contentDescription = "",
          modifier = Modifier
            .padding(start = 15.dp)
            .weight(0.1f)
            .clickable {
              videoPageViewModel.stopPlayer()
              onBackClick.invoke()
            },
        )
        Text(
          text = currentState.value.metaData.title.toString().removeFileExtension(),
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium,
          color = Color.White,
          maxLines = 1,
          modifier = Modifier
            .fillMaxWidth()
            .basicMarquee()
            .padding(vertical = 5.dp)
            .weight(0.9f),
        )
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .constrainAs(bottomSec) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
          },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color.Black.copy(alpha = 0.5f),
          contentColor = Color.White,
        ),
      ) {

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
        ) {
          PlayerController(
            modifier = Modifier
              .weight(0.2f, false),
            currentState = { currentState.value },
            onSeekToPrevious = { videoPageViewModel.seekToPrevious() },
            onSeekToNext = { videoPageViewModel.seekToNext() },
            onPause = { videoPageViewModel.pausePlayer() },
            onResume = { videoPageViewModel.resumePlayer() },
          )
          PlayerTimeLine(
            modifier = Modifier
              .fillMaxWidth()
              .weight(0.7f),
            currentState = { currentState.value },
            currentMediaPosition = currentPlayerPosition.toInt(),
            slideValueChangeFinished = {
              isSliderInteraction = false
              videoPageViewModel.seekToPosition(sliderValuePosition.toLong())
            },
            slideValueChange = { slideValue ->
              middleVideoPlayerIndicator = MiddleVideoPlayerIndicator.Seek(slideValue.toLong())
              sliderValuePosition = slideValue
              showInfoMiddleScreen = true
              isSliderInteraction = true
            },
          )
          if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            var iconResizeMode = remember {
              when (videoPageViewModel.playerResizeMode) {
                AspectRatioFrameLayout.RESIZE_MODE_FILL -> R.drawable.icon_fullscreen_24
                AspectRatioFrameLayout.RESIZE_MODE_FIT -> R.drawable.iocn_fullscreen_exit_24
                else -> 0
              }
            }
            PlayerControllerButton(
              icon = iconResizeMode,
              modifier = Modifier
                .size(25.dp)
                .weight(0.1f, false),
              onClick = {
                if (videoPageViewModel.playerResizeMode == AspectRatioFrameLayout.RESIZE_MODE_FILL) {
                  videoPageViewModel.playerResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                } else {
                  videoPageViewModel.playerResizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                  iconResizeMode = R.drawable.iocn_fullscreen_exit_24
                }
              },
            )
          }
        }
      }

    }

  }

}
