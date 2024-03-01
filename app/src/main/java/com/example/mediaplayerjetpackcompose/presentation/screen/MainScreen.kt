package com.example.mediaplayerjetpackcompose.presentation.screen

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerjetpackcompose.domain.model.navigation.BottomBarNavigationModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.navigation.BottomBarNavController
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.FullMusicPlayer
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.bottombar.BottomNavigationBar
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun MainScreen(
  navHostController: NavHostController = rememberNavController(),
  musicPageViewModel: MusicPageViewModel = koinViewModel(),
  videoPageViewModel: VideoPageViewModel = koinViewModel(),
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
          visible = currentRoute != BottomBarNavigationModel.VideoPlayerScreen.route,
          enter = slideInVertically(
            tween(durationMillis = 100),
            initialOffsetY = { int -> int / 2 },
          ),
          exit = slideOutVertically(
            tween(durationMillis = 100),
            targetOffsetY = { int -> int / 2 },
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
    Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) {
      BottomBarNavController(
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

    FullMusicPlayer(
      currentMediaCurrentState = currentMusicState,
      favoriteList = musicPageViewModel.favoriteListMediaId,
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
      onFavoriteToggle = { musicPageViewModel.handleFavoriteSongs(currentMusicState.mediaId) }
    )

  }

}

