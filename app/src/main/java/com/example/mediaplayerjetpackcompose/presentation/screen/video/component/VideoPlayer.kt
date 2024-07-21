package com.example.mediaplayerjetpackcompose.presentation.screen.video.component

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
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
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.decodeStringNavigation
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.FastSeekMode
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.MiddleVideoPlayerIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive

@kotlin.OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
  videoUri: String,
  videoPageViewModel: VideoPageViewModel,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  orientation: Int = LocalConfiguration.current.orientation,
  onBackClick: () -> Unit = {},
) {

  var playerSize by remember {
    mutableStateOf(IntSize.Zero)
  }
  var iconResizeMode by remember {
    mutableIntStateOf(R.drawable.icon_fullscreen_24)
  }
  var middleVideoPlayerIndicatorMutableState by remember {
    mutableStateOf<MiddleVideoPlayerIndicator>(MiddleVideoPlayerIndicator.Initial)
  }
  val currentPosition by videoPageViewModel.currentMediaPosition.collectAsStateWithLifecycle(
    initialValue = 0
  )
  var playerPadding by remember {
    mutableStateOf(0.dp)
  }
  var seekPosition by remember {
    mutableFloatStateOf(0f)
  }
  var onBackPress by remember {
    mutableStateOf(false)
  }
  var showInfoMiddleScreen by remember {
    mutableStateOf(false)
  }
  val currentState = videoPageViewModel.currentState.collectAsStateWithLifecycle()
  var playerControllerVisibility by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = showInfoMiddleScreen, key2 = seekPosition) {
    this.ensureActive()
    delay(1500L)
    showInfoMiddleScreen = false
  }

  LaunchedEffect(key1 = orientation, block = {
    when (orientation) {
      Configuration.ORIENTATION_LANDSCAPE -> {
        videoPageViewModel.deviceOrientation = AspectRatioFrameLayout.RESIZE_MODE_FIT
      }

      else -> {
        videoPageViewModel.deviceOrientation = AspectRatioFrameLayout.RESIZE_MODE_FIT
      }
    }
  })

  DisposableEffect(key1 = lifecycleOwner, effect = {
    val observe = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_DESTROY -> {
          videoPageViewModel.releasePlayer()
        }

        Lifecycle.Event.ON_START -> {
          videoPageViewModel.resumePlayer()
        }

        Lifecycle.Event.ON_STOP -> {
          videoPageViewModel.pausePlayer()
        }

        else -> {}
      }
    }
    lifecycleOwner.lifecycle.addObserver(observe)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observe)
    }
  })

  BackHandler(enabled = true) {
    onBackPress = true
  }

  LaunchedEffect(key1 = videoUri, block = {
    if (videoUri.isNotEmpty()) {
      videoPageViewModel.startPlayFromUri(videoUri.decodeStringNavigation())
    }
  })

  DisposableEffect(
    key1 = onBackPress,
    effect = {
      onDispose {
        videoPageViewModel.stopPlayer()
        onBackClick.invoke()
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
              offset.x > playerSize.width / 2 -> {
                videoPageViewModel.fastForward(15_000, currentPosition)
                middleVideoPlayerIndicatorMutableState =
                  MiddleVideoPlayerIndicator.FastSeek(FastSeekMode.FastForward)
                showInfoMiddleScreen = true
              }

              offset.x < playerSize.width / 2 -> {
                videoPageViewModel.fastRewind(15_000, currentPosition)
                middleVideoPlayerIndicatorMutableState =
                  MiddleVideoPlayerIndicator.FastSeek(FastSeekMode.FastRewind)
                showInfoMiddleScreen = true
              }
            }
          }
        )
      }
      .onGloballyPositioned { playerSize = it.size }
      .then(if (playerPadding != 0.dp) Modifier.displayCutoutPadding() else Modifier)
      .background(Color.Transparent),
    factory = {
      PlayerView(it).apply {
        player = videoPageViewModel.provideExoPlayer()
        useController = false
        resizeMode = videoPageViewModel.deviceOrientation
      }
    },
    update = {
      it.resizeMode = videoPageViewModel.deviceOrientation
    }
  )

  MiddleInfoHandler(
    modifier = Modifier,
    showInfoMiddleScreen = showInfoMiddleScreen,
    seekPosition = seekPosition.toLong(),
    middleVideoPlayerIndicator = middleVideoPlayerIndicatorMutableState,
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
        .displayCutoutPadding()
        .systemBarsPadding(),
    ) {
      val (topSec, bottomSec) = createRefs()

      Row(
        modifier = Modifier
          .constrainAs(topSec) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .background(Color.Transparent)
          .fillMaxWidth()
          .padding(start = 10.dp, end = 10.dp, top = 15.dp)
          .drawBehind {
            drawRoundRect(
              color = Color.Black,
              size = this.size,
              alpha = 0.4f,
              cornerRadius = CornerRadius(25f, 25f),
            )
          },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
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
        Spacer(modifier = Modifier.width(15.dp))
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


      Column(
        modifier = Modifier
          .constrainAs(bottomSec) {
            start.linkTo(parent.start, margin = 15.dp)
            end.linkTo(parent.end, margin = 15.dp)
            bottom.linkTo(parent.bottom, margin = 15.dp)
          }
          .fillMaxWidth()
          .padding(10.dp)
          .drawBehind {
            drawRoundRect(
              color = Color.Black,
              size = this.size,
              alpha = 0.4f,
              cornerRadius = CornerRadius(25f, 25f),
            )
          },
      ) {

        Row(
          horizontalArrangement = Arrangement.Absolute.SpaceBetween,
          modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth(),
        ) {
          Text(
            text = currentPosition.toInt().convertMilliSecondToTime(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
          )
          Text(
            text = (currentState.value.metaData.extras?.getInt("DURATION") ?: 0f).toInt()
              .convertMilliSecondToTime(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
          )
        }
        Slider(
          value = currentPosition.toFloat(),
          modifier = Modifier
            .fillMaxWidth(),
          onValueChange = { value ->
            showInfoMiddleScreen = true
            seekPosition = value
            middleVideoPlayerIndicatorMutableState = MiddleVideoPlayerIndicator.Seek(seekPosition.toLong())
            videoPageViewModel.seekToPosition(seekPosition.toLong())
          },
          valueRange = 0f..(currentState.value.metaData.extras?.getInt("DURATION")?.toFloat()
            ?: 0f),
          track = { sliderState ->
            SliderDefaults.Track(
              sliderState = sliderState,
              modifier = Modifier.scale(scaleX = 1f, scaleY = 3f),
              colors = SliderDefaults.colors(
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(0.5f),
              ),
            )
          },
          thumb = {},
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
          ),
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
        ) {
          CenterButton(
            icon = R.drawable.icon_fast_rewind_24,
            modifier = Modifier.size(35.dp),
            onClick = {
              videoPageViewModel.fastRewind(position = 15_000L, currentPosition = currentPosition)
            },
          )
          CenterButton(
            icon = R.drawable.icon_skip_previous_24,
            modifier = Modifier
              .size(40.dp),
            onClick = {
              videoPageViewModel.seekToPrevious()
            },
          )
          AnimatedContent(
            targetState = if (currentState.value.isPlaying) R.drawable.icon_pause_24 else R.drawable.icon_play_arrow_24,
            label = "",
          ) {
            CenterButton(
              icon = it,
              modifier = Modifier.size(55.dp),
              onClick = {
                when (currentState.value.isPlaying) {
                  true -> videoPageViewModel.pausePlayer()
                  false -> videoPageViewModel.resumePlayer()
                }
              },
            )
          }
          CenterButton(
            icon = R.drawable.icon_skip_next_24,
            modifier = Modifier.size(40.dp),
            onClick = {
              videoPageViewModel.seekToNext()
            },
          )
          CenterButton(
            icon = R.drawable.icon_fast_forward_24,
            modifier = Modifier
              .size(35.dp),
            onClick = {
              videoPageViewModel.fastForward(position = 15_000L, currentPosition = currentPosition)
            },
          )
          if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Spacer(modifier = Modifier.width(15.dp))
            CenterButton(
              icon = iconResizeMode,
              modifier = Modifier
                .size(25.dp),
              onClick = {
                if (videoPageViewModel.deviceOrientation == AspectRatioFrameLayout.RESIZE_MODE_FILL) {
                  videoPageViewModel.deviceOrientation = AspectRatioFrameLayout.RESIZE_MODE_FIT
                  playerPadding = 0.dp
                  iconResizeMode = R.drawable.icon_fullscreen_24
                } else {
                  videoPageViewModel.deviceOrientation = AspectRatioFrameLayout.RESIZE_MODE_FILL
                  playerPadding = 0.dp
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

@Composable
fun MiddleInfoHandler(
  modifier: Modifier,
  showInfoMiddleScreen: Boolean,
  seekPosition: Long,
  middleVideoPlayerIndicator: MiddleVideoPlayerIndicator,
) {
  AnimatedVisibility(
    visible = showInfoMiddleScreen,
    enter = fadeIn(),
    exit = fadeOut(),
    modifier = modifier
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      var text = ""
      var icon: Int? = 0
      when (middleVideoPlayerIndicator) {
        is MiddleVideoPlayerIndicator.FastSeek -> {
          text = middleVideoPlayerIndicator.seekMode.message
          icon = middleVideoPlayerIndicator.seekMode.icon
        }

        is MiddleVideoPlayerIndicator.Seek -> {
          text = seekPosition.toInt().convertMilliSecondToTime()
          icon = null
        }

        else -> {}
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .drawBehind {
            drawRoundRect(
              Color.Black,
              size = this.size,
              alpha = 0.5f,
              cornerRadius = CornerRadius(x = 25f, y = 25f)
            )
          }
          .padding(5.dp)
      ) {
        icon?.let {
          Icon(
            painter = painterResource(id = icon), contentDescription = "", Modifier.size(25.dp),
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
          text = text,
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
        )
      }
    }
  }

}


@Composable
fun CenterButton(
  icon: Int,
  onClick: () -> Unit,
  modifier: Modifier,
) {
  Image(
    painter = painterResource(id = icon),
    contentDescription = "",
    modifier = modifier
      .clickable(NoRippleEffect, null) {
        onClick.invoke()
      },
    colorFilter = ColorFilter.tint(Color.White),
  )
}