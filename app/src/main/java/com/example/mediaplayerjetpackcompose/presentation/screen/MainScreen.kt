package com.example.mediaplayerjetpackcompose.presentation.screen

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.BottomBarNavigationNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.component.NowMusicPlaying
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.BottomNavigationBar

@Composable
fun MainScreen(
  navHostController: NavHostController = rememberNavController(),
  videoPageViewModel: VideoPageViewModel = viewModel(factory = VideoPageViewModel.Factory),
  musicPageViewModel: MusicPageViewModel = viewModel(factory = MusicPageViewModel.Factory),
) {

  navHostController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
    initialValue = BottomBarNavigationModel.MusicScreen
  ).value
  val currentRoute = navHostController.currentDestination?.route
  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  val repeatMode = musicPageViewModel.currentRepeatMode.intValue
  val currentMusicPosition = musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle()
  val artworkImage = remember(currentMusicState) {
    musicPageViewModel.getImageArt(
      uri = currentMusicState.metaData.artworkUri ?: Uri.EMPTY,
    )
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    bottomBar = {
      if (currentRoute != null) {
        AnimatedVisibility(
          visible = currentRoute != BottomBarNavigationModel.PlayerScreen.route,
          enter = slideInVertically(
            tween(durationMillis = 50),
            initialOffsetY = { int -> int / 4 },
          ),
          exit = slideOutVertically(
            tween(durationMillis = 50),
            targetOffsetY = { int -> int / 4 },
          )
        ) {
          BottomNavigationBar(
            currentRoute = currentRoute,
            onClick = {
              navHostController.navigate(it)
            },
          )
        }
      }
    },
  ) { paddingValues ->
    if (musicPageViewModel.isLoading.value) {
      Surface(color = Color.Cyan, modifier = Modifier.fillMaxSize()) {

      }

    } else {
      Box(
        modifier = Modifier
          .padding(paddingValues)
          .fillMaxSize()
      ) {
        BottomBarNavigationNavController(
          navHostController = navHostController,
          musicPageViewModel = musicPageViewModel,
          videoPageViewModel = videoPageViewModel,
        )
      }
    }

  }





  AnimatedVisibility(
    visible = musicPageViewModel.showBottomSheet.value,
    enter = fadeIn(tween(300, 100)) + slideInVertically(
      animationSpec = tween(300),
      initialOffsetY = { int -> int / 4 }),
    exit = slideOutVertically(
      animationSpec = tween(300, 100),
      targetOffsetY = { int -> int / 4 }) + fadeOut(tween(300, 100))
  ) {

    NowMusicPlaying(
      currentMediaCurrentState = currentMusicState,
      artworkImage = artworkImage,
      repeatMode = repeatMode,
      currentMusicPosition = currentMusicPosition,
      onPauseMusic = musicPageViewModel::pauseMusic,
      onResumeMusic = musicPageViewModel::resumeMusic,
      onMoveNextMusic = musicPageViewModel::moveToNext,
      onMovePreviousMusic = musicPageViewModel::moveToPrevious,
      onSeekTo = { seekTo -> musicPageViewModel.seekToPosition(seekTo) },
      onRepeatMode = { repeat -> musicPageViewModel.setRepeatMode(repeat) },
      onDismissed = { musicPageViewModel.showBottomSheet.value = false },
    )

  }

}

