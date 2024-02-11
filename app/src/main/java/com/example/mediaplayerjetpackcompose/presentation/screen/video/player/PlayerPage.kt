package com.example.mediaplayerjetpackcompose.presentation.screen.video.player

import android.content.res.Configuration
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.decodeStringNavigation

@kotlin.OptIn(ExperimentalFoundationApi::class)
@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
  videoUri: String,
  onBackClick: () -> Unit = {},
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  orientation: Int = LocalConfiguration.current.orientation,
) {

  val playerViewModel = viewModel<PlayerViewModel>(factory = PlayerViewModel.Factory)
  val playerControllerVisibility = remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = videoUri, block = {
    // playerViewModel.getMediaInformationByUri(videoUri.decodeStringNavigation())
  })

  LaunchedEffect(key1 = orientation, block = {
    when (orientation) {
      Configuration.ORIENTATION_LANDSCAPE -> {
        playerViewModel.deviceOrientation.intValue = AspectRatioFrameLayout.RESIZE_MODE_FILL
      }

      else -> {
        playerViewModel.deviceOrientation.intValue = AspectRatioFrameLayout.RESIZE_MODE_FIT
      }
    }
  })

  DisposableEffect(key1 = lifecycleOwner, effect = {
    val observe = LifecycleEventObserver { _, event ->
      when (event) {
        Lifecycle.Event.ON_START -> {
          playerViewModel.resumePlayer()
        }

        Lifecycle.Event.ON_STOP -> {
          playerViewModel.pausePlayer()
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
    playerViewModel.onBackPress.value = true
  }

  LaunchedEffect(key1 = videoUri, block = {
    playerViewModel.startPlayVideo(videoUri.decodeStringNavigation())
  })

  DisposableEffect(
    key1 = playerViewModel.onBackPress.value,
    effect = {
      onDispose {
        playerViewModel.releasePlayer()
        onBackClick.invoke()
      }
    })

  AndroidView(
    modifier = Modifier
      .fillMaxSize()
      .clickable { playerControllerVisibility.value = !playerControllerVisibility.value }
      .background(color = Color.Black),
    factory = {
      PlayerView(it).apply {
        this.player = playerViewModel.exoPlayer
        this.useController = false
      }
    },
    update = {
      it.resizeMode = playerViewModel.deviceOrientation.intValue
    }
  )

  AnimatedVisibility(
    visible = playerControllerVisibility.value,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.weight(1f),
      ) {
        Image(
          painter = painterResource(id = R.drawable.icon_back),
          contentDescription = "",
          modifier = Modifier
            .clickable {
              playerViewModel.releasePlayer()
              onBackClick.invoke()
            }
            .padding(20.dp),
        )
        Spacer(modifier = Modifier.width(15.dp))
//        Text(
//          text = playerViewModel.mediaInformation.value.name,
//          fontSize = 16.sp,
//          fontWeight = FontWeight.Medium,
//          color = Color.White,
//          maxLines = 1,
//          modifier = Modifier.basicMarquee()
//        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.weight(8f),
      ) {

        Button(
          onClick = { },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          Image(
            painter = painterResource(id = R.drawable.icon_next),
            contentDescription = "",
            modifier = Modifier
              .size(30.dp)
              .rotate(180f),
            colorFilter = ColorFilter.tint(Color.White),
          )
        }
        Button(
          onClick = {
//            when (currentMusicState.isPlaying) {
//              true -> onPauseMusic.invoke()
//              false -> onResumeMusic.invoke()
//            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          AnimatedContent(
            // targetState = if (currentMusicState.isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            targetState = R.drawable.icon_pause,
            label = "",
          ) {
            Image(
              painter = painterResource(id = it),
              contentDescription = "",
              modifier = Modifier.size(35.dp),
              colorFilter = ColorFilter.tint(Color.White),
            )
          }
        }

        Button(
          onClick = {

          },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          Image(
            painter = painterResource(id = R.drawable.icon_next),
            contentDescription = "",
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Color.White),
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.weight(1f).padding(5.dp),
      ) {
        Text(
          text = "curernt",
          fontSize = 14.sp,
          fontWeight = FontWeight.Light,
        )
        Slider(
          value = 123f,
          onValueChange = {

          },
          valueRange = 0f .. 1000f,
        )
        Text(
          text = "curernt",
          fontSize = 14.sp,
          fontWeight = FontWeight.Light,
        )
      }
    }

  }
}