package com.example.mediaplayerjetpackcompose.presentation.screen

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicScreen
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.BottomNavigationNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.CollapsePlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component.NowMusicPlaying
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPage
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.player.PlayerScreen
import com.example.mediaplayerjetpackcompose.presentation.screenComponent.BottomNavigationBar

@Composable
fun MainScreen(
  navHostController: NavHostController = rememberNavController(),
) {

  navHostController.currentBackStackEntryFlow.collectAsStateWithLifecycle(
    initialValue = NavigationRoute.VideoScreen
  ).value
  val currentRoute = navHostController.currentDestination?.route
  val videoPageViewModel: VideoPageViewModel = viewModel(factory = VideoPageViewModel.Factory)
  val musicPageViewModel: MusicPageViewModel = viewModel(factory = MusicPageViewModel.Factory)

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
      if (currentRoute != null && currentRoute != NavigationRoute.PlayerScreen.route) {
        BottomNavigationBar(
          currentRoute = currentRoute,
          onClick = {
            navHostController.navigate(it)
          },
        )
      }
    },
  ) { paddingValues ->

    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
      BottomNavigationNavController(
        navHostController = navHostController,
        musicPageViewModel = musicPageViewModel,
        videoPageViewModel = videoPageViewModel,
      )
    }

  }

  AnimatedVisibility(
    visible = musicPageViewModel.showBottomSheet.value,
    enter = fadeIn() + slideInVertically(
      animationSpec = tween(300),
      initialOffsetY = { int -> int / 2 }),
    exit = slideOutVertically(
      animationSpec = tween(300),
      targetOffsetY = { int -> int / 2 }) + fadeOut()
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

sealed class NavigationRoute(var route: String) {
  data object VideoScreen : NavigationRoute("VideoScreen")
  data object MusicScreen : NavigationRoute("MusicScreen")
  data object PlayerScreen : NavigationRoute("PlayerScreen/{videoUri}")
}