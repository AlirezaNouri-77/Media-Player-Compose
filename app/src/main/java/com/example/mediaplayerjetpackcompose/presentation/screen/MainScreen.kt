package com.example.mediaplayerjetpackcompose.presentation.screen

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.BottomBarNavigationNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.component.NowMusicPlaying
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.BottomNavigationBar

@Composable
fun MainScreen(
  navHostController: NavHostController = rememberNavController(),
  videoPageViewModel: VideoPageViewModel = viewModel(factory = VideoPageViewModel.Factory),
  musicPageViewModel: MusicPageViewModel = viewModel(factory = MusicPageViewModel.Factory)
) {

  navHostController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
    initialValue = BottomBarNavigationModel.VideoScreen
  ).value
  val currentRoute = navHostController.currentDestination?.route

  val currentMusicState = musicPageViewModel.currentMusicState.collectAsStateWithLifecycle().value
  val currentMusicPosition =
    musicPageViewModel.currentMusicPosition.collectAsStateWithLifecycle()
  val artworkImage = remember(currentMusicState) {
    musicPageViewModel.getImageArt(
      uri = currentMusicState.metadata.artworkUri ?: Uri.EMPTY,
    )
  }

  Scaffold(
    contentWindowInsets = WindowInsets(0.dp),
    bottomBar = {
      if (currentRoute != null && currentRoute != BottomBarNavigationModel.PlayerScreen.route) {
        BottomNavigationBar(
          currentRoute = currentRoute,
          onClick = {
            navHostController.navigate(it)
          },
        )
      }
    },
  ) { paddingValues ->
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
      currentMusicState = currentMusicState,
      artworkImage = artworkImage,
      currentMusicPosition = currentMusicPosition,
      onPauseMusic = musicPageViewModel::pauseMusic,
      onResumeMusic = musicPageViewModel::resumeMusic,
      onMoveNextMusic = musicPageViewModel::moveToNext,
      onMovePreviousMusic = musicPageViewModel::moveToPrevious,
      onSeekTo = { seekTo -> musicPageViewModel.seekToPosition(seekTo) },
      onDismissed = { musicPageViewModel.showBottomSheet.value = false },
    )
  }

}

