package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.decodeStringNavigation
import com.example.mediaplayerjetpackcompose.data.removeFileExtension
import com.example.mediaplayerjetpackcompose.presentation.screen.component.ControlBottom
import com.example.mediaplayerjetpackcompose.presentation.util.NoRippleEffect

@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerPage(
  videoUri: String,
  videoPageViewModel: VideoPageViewModel,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  activity: Activity = LocalContext.current as Activity,
  orientation: Int = LocalConfiguration.current.orientation,
  onBackClick: () -> Unit = {},
) {

  val currentPosition by videoPageViewModel.currentMediaPosition.collectAsStateWithLifecycle(
    initialValue = 0
  )
  var seekPosition by remember {
    mutableFloatStateOf(0f)
  }
  var onBackPress by remember {
    mutableStateOf(false)
  }
  var sliderInInteraction by remember {
    mutableStateOf(false)
  }
  val currentState = videoPageViewModel.currentState.collectAsStateWithLifecycle()
  var playerControllerVisibility by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = orientation, block = {
    when (orientation) {
      Configuration.ORIENTATION_LANDSCAPE -> {
        videoPageViewModel.deviceOrientation.intValue = AspectRatioFrameLayout.RESIZE_MODE_FILL
      }

      else -> {
        videoPageViewModel.deviceOrientation.intValue = AspectRatioFrameLayout.RESIZE_MODE_FIT
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
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
      }
    })

  AndroidView(
    modifier = Modifier
      .fillMaxSize()
      .clickable { playerControllerVisibility = !playerControllerVisibility }
      .systemBarsPadding()
      .displayCutoutPadding()
      .background(Color.Transparent),
    factory = {
      PlayerView(it).apply {
        this.player = videoPageViewModel.exoPlayer
        this.useController = false
      }
    },
    update = {
      it.resizeMode = videoPageViewModel.deviceOrientation.intValue
    }
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
        .systemBarsPadding()
        .displayCutoutPadding(),
    ) {
      val (topSec, bottomSec, seekTo) = createRefs()

      Row(
        modifier = Modifier
          .constrainAs(topSec) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .background(Color.Transparent)
          .fillMaxWidth()
          .padding(horizontal = 10.dp)
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

      AnimatedVisibility(
        visible = sliderInInteraction,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Text(
          text = seekPosition.toInt().convertMilliSecondToTime(),
          color = Color.White,
          modifier = Modifier
            .constrainAs(seekTo) {
              start.linkTo(parent.start)
              end.linkTo(parent.end)
              top.linkTo(parent.top)
              bottom.linkTo(parent.bottom)
            }
            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
            .padding(15.dp),
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
          onValueChange = {
            sliderInInteraction = true
            seekPosition = it
          },
          onValueChangeFinished = {
            sliderInInteraction = false
            videoPageViewModel.seekToPosition(seekPosition.toLong())
          },
          valueRange = 0f..(currentState.value.metaData.extras?.getInt("DURATION")?.toFloat()
            ?: 0f),
          modifier = Modifier.padding(horizontal = 7.dp),
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.White.copy(0.6f),
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
          Spacer(modifier = Modifier.width(10.dp))
          CenterButton(
            icon = R.drawable.icon_screen_rotation_alt_24,
            modifier = Modifier
              .size(25.dp),
            onClick = {
              when (orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                  activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }

                Configuration.ORIENTATION_PORTRAIT -> {
                  activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
              }
            },
          )
        }
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